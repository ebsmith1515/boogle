package ui;

import client.BoggleClient;
import java.io.IOException;
import java.net.InetAddress;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import server.BoggleServer;

public class StartGameController {

	private MainController mainController;

	protected StartGame startGame;

	public StartGameController(MainController mainController) {
		startGame = new StartGame(this);
		this.mainController = mainController;
		String lastUsed = getLastUsedIP();
		String tooltip = "Get this from the user that is starting the server. Something like '192.123.4.56'. Not needed if you are starting the server.";
		startGame.ipField.setToolTipText(tooltip);
		startGame.ipLabel.setToolTipText(tooltip);
		if (lastUsed != null) {
			startGame.ipField.setText(lastUsed);
		}
	}

	private String getLastUsedIP() {
		return Preferences.userNodeForPackage(this.getClass()).get("ip", null);
	}

	private void saveIP(String ip) {
		Preferences.userNodeForPackage(this.getClass()).put("ip", ip);
	}

	public void resetGame() {
		startGame.message.setText("");
		startGame.joinButton.setEnabled(true);
		startGame.startButton.setEnabled(true);
		startGame.numPlayers.setText("");
	}

	public void joinGame() {
		startGame.message.setText("");
		mainController.client = new BoggleClient(mainController);
		String ipAddress = null;
		if (!startGame.ipField.getText().isEmpty()) {
			ipAddress = startGame.ipField.getText();
		}

		if (getName().equals("")) {
			startGame.message.setText("Please enter a name.");
		} else {
			if (mainController.client.connect(ipAddress)) {
				if (ipAddress != null) {
					saveIP(ipAddress);
				}
				startGame.joinButton.setEnabled(false);
				startGame.startButton.setEnabled(false);
				mainController.client.start();
				mainController.client.sendName(getName());
			} else {
				startGame.message.setText("Connection refused. Try again.");
			}
		}
	}

	public String getName() {
		return startGame.nameField.getText().replaceAll(" ", "").replaceAll(BoggleServer.CMD_DELIM, "");
	}

	public void initServer() {
		if (getName().equals("")) {
			startGame.message.setText("Please enter a name.");
		} else {
			if (mainController.server != null) {
				startGame();
				return;
			}
			mainController.server = new BoggleServer(this);
			mainController.server.start();
			startGame.numPlayers.setVisible(true);
			startGame.ipField.setText(null);
			joinGame();
			String address = "unknown";
			try {
				address = InetAddress.getLocalHost().getHostAddress();
			} catch (IOException ignored) {

			}
			startGame.message.setText("<html>Waiting for others to connect at <b>" + address + "</b><br />Click start again to begin.</html>");
		}

		startGame.startButton.setEnabled(true);
	}

	public void updateNumPlayers(final int playerCount) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGame.numPlayers.setText("Number of Players: " + playerCount);
			}
		});
	}

	private void startGame() {
		startGame.message.setText("Starting game...");
		mainController.server.startGame();
	}
}
