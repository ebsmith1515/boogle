package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.*;
import org.apache.log4j.Logger;

public class BoggleServer extends Thread {

	private static final Logger	log = Logger.getLogger(BoggleServer.class);

	public static final int PORT = 9191;
	private static final int GAME_SECONDS = 120;
	protected static final String THE_SERVER_NAME = "<The Server>";
	boolean waitingForPlayers = true;
	public static final String CMD_DELIM = " ";
	public static final String CHAT_DELIM = "%%";

	private ServerSocket listener;
	private List<Player> players;
	private Results results;
	private boolean running;
	private BoardGenerator boardGenerator;

	//TODO:
	/*
		1. DONE Bug where one player leaves after all others have clicked ready.
		2. DONE Words table needs to fill the whole space.
		3. CHECK AGAIN Total score should be allowed to be negative.
		4. DONE Reset should say something else. Back.
		5. Tooltip to describe scoring
		6. DONE Duplicates should not just use hyphens.
		7. Pre-generate the board.
		8. Messing up when someone is in waiting room for a while.
		9. Keep alive

		Nice to have:
		1. Notify all players when one player gets a long word.
		2.
	 */

	public static void main(String[] args) {
		BoggleServer server = new BoggleServer();
		server.newGame();
	}

	public void checkNextRound() {
		boolean allReady = true;
		for (Player player : players) {
			if (!player.isReady()) {
				allReady = false;
			}
		}
		if (allReady && !players.isEmpty()) {
			nextRound();
		}
	}

	public void addChat(Player fromPlayer, String message) {
		addChat(fromPlayer.getPlayerName(), message);
	}

	private void addChat(String playerName, String message) {
		for (Player player : players) {
			player.sendChat(playerName, message);
		}
	}

	public enum Commands {
		PING,START,WORDS,NAME,RESULTS,SCORES,CHAT,ALLWORDS,NOTACCEPTING,END
	}

	public BoggleServer() {
		players = new ArrayList<Player>();
		boardGenerator = new BoardGenerator();
	}

	@Override
	public void run() {
		newGame();
	}

	public void stopBoggle() {
		running = false;
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Player player : players) {
			player.stopGame();
		}
	}

	public void newGame() {
		running = true;
		players = new ArrayList<Player>();
		results = null;
		startKeepAliveCheckThread();
		try {
			listener = new ServerSocket(PORT, 0, new InetSocketAddress("0.0.0.0", PORT).getAddress());
			int playerNum = 1;
			while (true) {
				Player player = new Player(listener.accept(), this, playerNum++);
				if (waitingForPlayers) {
					addPlayer(player);
					player.start();
					log.debug("Adding new player");
				} else {
					player.send(Commands.NOTACCEPTING.toString());
				}
			}
		} catch (SocketException ex) {
			log.debug("Socket closed");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void startKeepAliveCheckThread() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Player deadPlayer = null;
				for (Player player : players) {
					if (System.currentTimeMillis() - player.getLastPingTime() > 10000) {
						deadPlayer = player;
					}
				}
				if (deadPlayer != null) {
					removePlayer(deadPlayer);
				}
			}
		};
		new Timer().scheduleAtFixedRate(task, 0, 10000);
	}

	private void sendAllWords() {
		String wordsString = Commands.ALLWORDS.toString();
		List<Integer> sortedNumList = new ArrayList<Integer>(boardGenerator.wordsOnBoard.keySet());
		Collections.sort(sortedNumList);
		Collections.reverse(sortedNumList);

		for (int len : sortedNumList) {
			List<String> wordList = boardGenerator.wordsOnBoard.get(len);
			for (String word : wordList) {
				wordsString += CMD_DELIM + word;
			}
		}

		broadcast(wordsString);
	}

	protected void sendScores() {
		String scoreString = Commands.SCORES.toString();
		for (Player player : players) {
			scoreString += CMD_DELIM + player.getPlayerName() +
					CMD_DELIM + player.getScore() + CMD_DELIM + player.getLastScore();
		}
		broadcast(scoreString);
	}

	private int secondsLeft;
	public void nextRound() {
		secondsLeft = 5;

		new Thread(boardGenerator).start();

		final Timer repeatingTimer = new Timer();
		repeatingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				addChat(THE_SERVER_NAME, "Starting game: " + secondsLeft + "...");
				secondsLeft--;
				if (secondsLeft < 0) {
					repeatingTimer.cancel();
				}
			}
		}, 1000, 1000);

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				sentResults = false;
				for (Player player : players) {
					player.nextRound();
				}
				startGame();
			}
		}, 6000);
	}

	public void addPlayer(Player player) {
		players.add(player);
		sendScores();
	}

	public void removePlayer(Player player) {
		log.debug("removing player " + player.getPlayerName());
		players.remove(player);
		if (players.isEmpty()) {
			waitingForPlayers = true;
		}
		if (waitingForPlayers) {
			sendScores();
			checkNextRound();
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void startGame() {
		if (!players.isEmpty()) {
			waitingForPlayers = false;

			int gameSeconds = GAME_SECONDS;
			if (players.get(0).getPlayerName().startsWith("_SHORT")) {
				gameSeconds = Integer.parseInt(players.get(0).getPlayerName().substring(6));
			}
			while (!boardGenerator.done) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			broadcast(Commands.START.toString() + CMD_DELIM + gameSeconds + CMD_DELIM + boardGenerator.gameLetters);
		}
	}

	public void broadcast(String message) {
		for (Player player : players) {
			player.send(message);
		}
	}

	private boolean sentResults = false;
	public synchronized void checkEnd() {
		boolean allDone = true;
		for (Player player : players) {
			if (player.getEnteredWords() == null) {
				allDone = false;
				break;
			}
		}
		if (allDone && !sentResults) {
			log.debug("All done, sending results");
			waitingForPlayers = true;
			sentResults = true;
			results = new Results(players);
			for (Player player : players) {
				for (String word : player.getEnteredWords()) {
					removeFromWordsOnBoard(word);
					results.addResult(player.getPlayerName(), word);
				}
			}
			sendResults();
			calculateScores();
			sendScores();
			sendAllWords();
		}
	}

	private void removeFromWordsOnBoard(String word) {
		for (List<String> wordList : boardGenerator.wordsOnBoard.values()) {
			wordList.remove(word);
		}
	}

	private void calculateScores() {
		for (Player player : players) {
			List<Results.Result> playerResults = results.getPlayerResults().get(player.getPlayerName());
			for (Results.Result result : playerResults) {
				if (!result.isInvalid() && !result.isCancelled()) {
					if (result.getWord().length() < 5) {
						player.incrementScore(1);
					} else if (result.getWord().length() == 5) {
						player.incrementScore(2);
					} else if (result.getWord().length() == 6) {
						player.incrementScore(3);
					} else if (result.getWord().length() == 7) {
						player.incrementScore(5);
					} else if (result.getWord().length() > 7) {
						player.incrementScore(11);
					}
				} else if (result.isInvalidDictionary()) {
					player.incrementScore(-1);
				}
			}
		}
	}

	protected void sendResults() {
		if (results == null) {
			results = new Results(players);
		} else {
			results.processResults(boardGenerator.gameLetters);
		}
		broadcast(Commands.RESULTS + CMD_DELIM + results.serialize());
	}

	public Results getResults() {
		return results;
	}

	public boolean isRunning() {
		return running;
	}
}
