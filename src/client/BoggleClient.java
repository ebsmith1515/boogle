package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import javax.swing.SwingUtilities;
import server.BoggleServer;
import static server.BoggleServer.CMD_DELIM;
import static server.BoggleServer.Commands.*;

import server.Results;
import ui.Main;
import ui.MainController;

public class BoggleClient extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
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
				} else if (line.startsWith(SCORES.toString())) {
					String scoreStr = line.substring((SCORES.toString() + " ").length());
					mainController.showScores(scoreStr);
				} else if (line.startsWith(ALLWORDS.toString())) {
					String wordStr = line.substring((ALLWORDS.toString() + " ").length());
					mainController.showAllWords(wordStr.split(CMD_DELIM));
				} else if (line.startsWith(CHAT.toString())) {
					System.out.println(line);
					String[] lineSplit = line.split(CMD_DELIM, 2);
					String player = lineSplit[1].split(BoggleServer.CHAT_DELIM)[0];
					String message = lineSplit[1].split(BoggleServer.CHAT_DELIM)[1];
					mainController.addChatMessage(player, message);
				} else if (line.startsWith(NOTACCEPTING.toString())) {
					mainController.notAccepting();
				}
			}
		} catch (SocketException ex) {
			System.out.println("socket closed");
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
		boolean success = false;
		try {
			socket = new Socket(serverAddress, BoggleServer.PORT);
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
		out.println(PING);
	}

	public void stopBoggle() {
		running = false;
		if (out != null) {
			try {
				out.println(END.toString());
				socket.close();
			} catch (IOException e) {
				System.out.println("error closing socket.");
			}
		}
	}

	public void sendName(String name) {
		out.println(BoggleServer.Commands.NAME.toString() + CMD_DELIM + name + CMD_DELIM + Main.VERSION);
	}

	public void nextRound() {
		out.println(BoggleServer.Commands.START);
		sendChat("<ready for next round>");
	}

	public void sendChat(String text) {
		out.println(BoggleServer.Commands.CHAT + CMD_DELIM + text);
	}
}
