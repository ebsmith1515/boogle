package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.SwingUtilities;
import server.BoggleServer;
import ui.MainController;

public class BoggleClient extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	MainController mainController;

	public BoggleClient(MainController mainController) {
		this.mainController = mainController;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line.startsWith(BoggleServer.Commands.START.toString())) {
					System.out.println("Received START from server.");
					String timeString = line.split(BoggleServer.CMD_DELIM)[1];
					final int gameSeconds;
					try {
						gameSeconds = Integer.parseInt(timeString);
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								mainController.showGameBoard(gameSeconds);
							}
						});
					} catch (NumberFormatException ex) {
						System.out.println("Could not parse to int: " + timeString);
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean connect(String serverAddress) {
		if (serverAddress == null) {
			serverAddress = "localhost";
		}
		boolean success = false;
		try {
			Socket socket = new Socket(serverAddress, BoggleServer.PORT);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public void pingServer() {
		out.println("PING");
	}

}
