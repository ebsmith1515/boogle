package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by esmith on 4/5/15.
 */
public class BoggleServer extends Thread {

	public static final int PORT = 9191;
	boolean waitingForPlayers = true;

	List<Player> players = new ArrayList<Player>();

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
}
