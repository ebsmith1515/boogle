package ui;

import client.BoggleClient;
import server.BoggleServer;

import java.util.prefs.Preferences;

public class StartGameController {

	private MainController mainController;

	protected StartGame startGame;

	public StartGameController(MainController mainController) {
		startGame = new StartGame(this);
		this.mainController = mainController;
	}

	public void resetGame() {
		startGame.message.setText("");
		startGame.enterButton.setEnabled(true);
	}

	public void joinGame() {
		startGame.message.setText("");
		mainController.client = new BoggleClient(mainController);
//		String ipAddress = "65.60.197.125";
		String ipAddress = "localhost";
		if (getName().equals("")) {
			startGame.message.setText("Please enter a name.");
		} else {
			if (mainController.client.connect(ipAddress)) {
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

	/*public void initServer() {
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
	}*/

	/*public void updateNumPlayers(final int playerCount) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGame.numPlayers.setText("Number of Players: " + playerCount);
			}
		});
	}*/

	private void startGame() {
		startGame.message.setText("Starting game...");
		mainController.server.startGame();
	}
}
