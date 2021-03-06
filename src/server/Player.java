package server;

import org.apache.log4j.Logger;
import ui.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static server.BoggleServer.CMD_DELIM;
import static server.BoggleServer.Commands.*;

public class Player extends Thread {

	private final static Logger log = Logger.getLogger(Player.class);

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private List<String> enteredWords;
	private BoggleServer server;
	private String playerName;
	private int score;
	private int lastScore;
	private Status status;
	private long lastPingTime;

	enum Status {
		PLAYING, WAITING, READY
	}

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

	public void nextRound() {
		enteredWords = null;
		status = Status.PLAYING;
		lastScore = 0;
	}

	public void send(String message) {
		if (output != null) {
			output.println(message);
		}
	}

	@Override
	public void run() {
		log.debug("Starting player thread.");
		boolean done = false;
		try {
			while (!done && server.isRunning()) {
				String line = input.readLine();
				if (line != null) {
					if (line.startsWith(PING.toString())) {
						lastPingTime = System.currentTimeMillis();
					} else if (line.startsWith(WORDS.toString())) {
						handleWords(line);
						server.checkEnd();
					} else if (line.startsWith(NAME.toString())) {
						String[] nameVersion = line.split(CMD_DELIM);
						if (nameVersion.length == 3) {
							setVersion(nameVersion[2]);
						} else {
							//pre version check client
							setVersion("0");
						}
						setPlayerName(nameVersion[1]);
					} else if (line.equals(START.toString())) {
						status = Status.READY;
						server.checkNextRound();
					} else if (line.startsWith(CHAT.toString())) {
						server.addChat(this, line.split(CMD_DELIM, 2)[1]);
					} else if (line.startsWith(END.toString())) {
						done = true;
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				log.debug("socket closed for " + playerName);
				socket.close();
				server.removePlayer(this);
			} catch (IOException ignored) {
			}
		}
	}

	public long getLastPingTime() {
		return lastPingTime;
	}

	public void handleWords(String cmdLine) {
		if (cmdLine.equals(WORDS.toString())) {
			enteredWords = new ArrayList<String>();
			return;
		}
		cmdLine = cmdLine.replaceFirst(WORDS.toString() + CMD_DELIM, "");
		String[] cmdSplit = cmdLine.split(CMD_DELIM);
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
		log.debug("Set playerName=" + playerName);
		server.sendScores();
	}

	protected void setVersion(String versionStr) {
		boolean goodVersion = false;
		try {
			int versionFromClient = Integer.parseInt(versionStr);
			if (versionFromClient >= Main.VERSION) {
				goodVersion = true;
			}
		} catch (NumberFormatException ex) {
			log.error(ex.getMessage(), ex);
		}
		if (goodVersion) {
			sendChat(BoggleServer.THE_SERVER_NAME, "Welcome!");
		} else {
			sendChat(BoggleServer.THE_SERVER_NAME, "A new version of Boogle is ready to download " +
					"at booglegame.com! This version will definitely not work, but you can try for fun if you want.");
		}
	}

	public int getScore() {
		return score;
	}

	public void incrementScore(int amount) {
		score += amount;
		lastScore += amount;
	}

	public boolean isReady() {
		return status == Status.READY;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getLastScore() {
		return lastScore;
	}

	public void sendChat(String fromPlayer, String message) {
		output.println(CHAT + CMD_DELIM + fromPlayer + BoggleServer.CHAT_DELIM + message);
	}

	public void stopGame() {
		try {
			socket.close();
		} catch (IOException e) {
			log.error("error closing socket.", e);
		}
	}
}
