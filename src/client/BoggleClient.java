package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingUtilities;
import server.BoggleServer;
import static server.BoggleServer.CMD_DELIM;
import static server.BoggleServer.Commands.*;
import server.Results;
import ui.MainController;

public class BoggleClient extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	MainController mainController;
	private boolean running = true;

	public BoggleClient(MainController mainController) {
		this.mainController = mainController;
	}

	@Override
	public void run() {
		try {
			while (running) {
				String line = in.readLine();
				if (line.startsWith(START.toString())) {
					System.out.println("Received START from server.");
					String timeString = line.split(CMD_DELIM)[1];
					final String letters = line.split(CMD_DELIM)[2];
					final int gameSeconds;
					try {
						gameSeconds = Integer.parseInt(timeString);
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								mainController.showGameBoard(gameSeconds, letters);
							}
						});
					} catch (NumberFormatException ex) {
						System.out.println("Could not parse to int: " + timeString);
					}
				} else if (line.startsWith(RESULTS.toString())) {
					String resultsStr = line.split(CMD_DELIM)[1];
					Results results = new Results(resultsStr);
					mainController.showResults(results);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException ignored) {
			}
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

	public void sendWords(List<String> words) {
		String wordsCommand = WORDS.toString();
		for(String word : words) {
			wordsCommand += CMD_DELIM + word;
		}
		out.println(wordsCommand);
	}

	public void pingServer() {
		out.println("PING");
	}

	public void stopBoggle() {
		running = false;
	}
}
