package server;

import java.io.IOException;
import java.net.ServerSocket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BoggleServer extends Thread {

	public static final int PORT = 9191;
	boolean waitingForPlayers = true;

	private ObservableList<Player> players = FXCollections.observableArrayList();

	public enum Commands {
		START
	}

	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(PORT);
			while (waitingForPlayers) {
				Player player = new Player(listener.accept());
				player.start();
				players.add(player);
				System.out.println("Adding new player");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ObservableList<Player> getPlayers() {
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
