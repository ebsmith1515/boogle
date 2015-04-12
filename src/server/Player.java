package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static server.BoggleServer.Commands.NAME;
import static server.BoggleServer.Commands.WORDS;

public class Player extends Thread {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private List<String> enteredWords;
	private BoggleServer server;
	private String playerName;

	public Player(int playerNum) {
		this.playerName = "Player" + playerNum;
	}

	public Player(Socket socket, BoggleServer server, int playerNum) {
		this.playerName = "Player" + playerNum;
		this.socket = socket;
		this.server = server;
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String message) {
		if (output != null) {
			output.println(message);
		}
	}

	@Override
	public void run() {
		System.out.println("Starting player thread.");
		try {
			while (true) {
				String line = input.readLine();
				if (line.startsWith("PING")) {
					output.println("WELCOME");
				} else if (line.startsWith(WORDS.toString())) {
					handleWords(line);
					server.checkEnd();
				} else if (line.startsWith(NAME.toString())) {
					setPlayerName(line.split(BoggleServer.CMD_DELIM)[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException ignored) {}
		}
	}

	public void handleWords(String cmdLine) {
		if (cmdLine.equals(WORDS.toString())) {
			enteredWords = new ArrayList<String>();
			return;
		}
		cmdLine = cmdLine.replaceFirst(WORDS.toString() + BoggleServer.CMD_DELIM, "");
		String[] cmdSplit = cmdLine.split(BoggleServer.CMD_DELIM);
		enteredWords = new ArrayList<String>(Arrays.asList(cmdSplit));
	}

	public List<String> getEnteredWords() {
		return enteredWords;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
