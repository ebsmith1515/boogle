package ui;

import client.BoggleClient;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.SwingUtilities;
import server.BoggleServer;

public class StartGameController {

	private MainController mainController;

	protected StartGame startGame;

	public StartGameController(MainController mainController) {
		startGame = new StartGame(this);
		this.mainController = mainController;
		startGame.ipField.setText("192.168.1.134");
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
			startGame.ipField.setText("localhost");
			mainController.server = new BoggleServer(this);
			mainController.server.start();
			startGame.numPlayers.setVisible(true);
			joinGame();
			String address = "unknown";
			try {
				address = InetAddress.getLocalHost().getHostAddress();
			} catch (IOException ignored) {

			}
			startGame.message.setText("<html>Waiting for others to connect at " + address + "<br />Click start again to begin.</html>");
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
