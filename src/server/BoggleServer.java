package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.*;

import ui.StartGameController;

public class BoggleServer extends Thread {

	public static final int PORT = 9191;
	private static final int GAME_SECONDS = 120;
	boolean waitingForPlayers = true;
	public static final String CMD_DELIM = " ";
	public static final String CHAT_DELIM = "%%";

	private ServerSocket listener;
	private List<Player> players;
	protected StartGameController startGameController;
	private Results results;
	private String gameLetters;
	private WordChecker wordChecker;
	private HashMap<Integer, List<String>> wordsOnBoard;
	private boolean running;

	public void checkNextRound() {
		boolean allReady = true;
		for (Player player : players) {
			if (!player.isReady()) {
				allReady = false;
			}
		}
		if (allReady) {
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
		START,WORDS,NAME,RESULTS,SCORES,CHAT,ALLWORDS
	}

	public BoggleServer() {

	}

	public BoggleServer(StartGameController startGameController) {
		this.startGameController = startGameController;
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
		try {
			listener = new ServerSocket(PORT, 0, new InetSocketAddress("0.0.0.0", PORT).getAddress());
			int playerNum = 1;
			while (waitingForPlayers) {
				Player player = new Player(listener.accept(), this, playerNum++);
				player.start();
				addPlayer(player);
				System.out.println("Adding new player");
			}
		} catch (SocketException ex) {
			System.out.println("Socket closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAllWords() {
		String wordsString = Commands.ALLWORDS.toString();
		List<Integer> sortedNumList = new ArrayList<Integer>(wordsOnBoard.keySet());
		Collections.sort(sortedNumList);
		Collections.reverse(sortedNumList);

		for (int len : sortedNumList) {
			List<String> wordList = wordsOnBoard.get(len);
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

		final Timer repeatingTimer = new Timer();
		repeatingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				addChat("<The Server>", "Starting game: " + secondsLeft + "...");
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
		startGameController.updateNumPlayers(players.size());
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void startGame() {
		gameLetters = new LetterFactory().getLetterList();
		wordChecker = new WordChecker(gameLetters);
		int gameSeconds = GAME_SECONDS;
		if (players.get(0).getPlayerName().startsWith("_SHORT")) {
			gameSeconds = Integer.parseInt(players.get(0).getPlayerName().substring(6));
		}

		broadcast(Commands.START.toString() + CMD_DELIM + gameSeconds + CMD_DELIM + gameLetters);
		findAllWordsThread();
	}

	private void findAllWordsThread() {
		new Thread() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				wordsOnBoard = new HashMap<Integer, List<String>>();
				try {
					WordList wordList = WordList.getInstance();
					for (String word : wordList.dictionary.keySet()) {
						if (word.length() > 2 && wordChecker.checkWord(word)) {
							putInWordsOnBoard(word.length(), word);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				long seconds = (System.currentTimeMillis() - startTime) / 1000;
				System.out.println("Ran findAllWords in " + seconds + " seconds.");
			}
		}.start();
	}

	private void putInWordsOnBoard(int num, String word) {
		if (!wordsOnBoard.containsKey(num)) {
			wordsOnBoard.put(num, new ArrayList<String>());
		}
		wordsOnBoard.get(num).add(word);
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
			System.out.println("All done, sending results");
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
		for (List<String> wordList : wordsOnBoard.values()) {
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
				}
			}
		}
	}

	private void sendResults() {
		results.processResults(gameLetters);
		broadcast(Commands.RESULTS + CMD_DELIM + results.serialize());
	}

	public Results getResults() {
		return results;
	}

	public boolean isRunning() {
		return running;
	}
}
