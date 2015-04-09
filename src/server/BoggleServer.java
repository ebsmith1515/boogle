package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import ui.StartGameController;

public class BoggleServer extends Thread {

	public static final int PORT = 9191;
	boolean waitingForPlayers = true;

	private List<Player> players = new ArrayList<Player>();
	protected StartGameController startGameController;

	public enum Commands {
		START
	}

	public BoggleServer(StartGameController startGameController) {
		this.startGameController = startGameController;
	}

	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(PORT);
			while (waitingForPlayers) {
				Player player = new Player(listener.accept());
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
		broadcast(Commands.START.toString());
	}

	public void broadcast(String message) {
		for (Player player : players) {
			player.send(message);
		}
	}
}
