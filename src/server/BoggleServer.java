package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import ui.StartGameController;

public class BoggleServer extends Thread {

	public static final int PORT = 9191;
	private static final int GAME_SECONDS = 120;
	boolean waitingForPlayers = true;
	public static final String CMD_DELIM = " ";

	private List<Player> players = new ArrayList<Player>();
	protected StartGameController startGameController;
	private Results results;

	public enum Commands {
		START,WORDS,NAME,RESULTS
	}

	public BoggleServer() {

	}

	public BoggleServer(StartGameController startGameController) {
		this.startGameController = startGameController;
	}

	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(PORT, 0, new InetSocketAddress("0.0.0.0", PORT).getAddress());
			int playerNum = 1;
			while (waitingForPlayers) {
				Player player = new Player(listener.accept(), this, playerNum++);
				player.start();
				addPlayer(player);
				System.out.println("Adding new player");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPlayer(Player player) {
		players.add(player);
		startGameController.updateNumPlayers(players.size());
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void startGame() {
		String letters = new LetterFactory().getLetterList();
		broadcast(Commands.START.toString() + CMD_DELIM + GAME_SECONDS + CMD_DELIM + letters);
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
					results.addResult(player.getPlayerName(), word);
				}
			}
			sendResults();
		}
	}

	private void sendResults() {
		results.sortResults();
		broadcast(Commands.RESULTS + CMD_DELIM + results.serialize());
	}

	public Results getResults() {
		return results;
	}
}
