package ui;

import client.BoggleClient;
import javax.swing.SwingUtilities;
import server.BoggleServer;

public class StartGameController {

	private BoggleServer server;
	private BoggleClient client;
	private MainController mainController;


	protected StartGame startGame;

	public StartGameController(MainController mainController) {
		startGame = new StartGame(this);
		this.mainController = mainController;
	}

	public void joinGame() {
		client = new BoggleClient(mainController);
		String ipAddress = null;
		if (!startGame.ipField.getText().isEmpty()) {
			ipAddress = startGame.ipField.getText();
		}
		if (client.connect(ipAddress)) {
			startGame.joinButton.setEnabled(false);
			startGame.startButton.setEnabled(false);
			client.start();
		} else {
			startGame.message.setText("Connection refused. Try again.");
		}
	}

	public void initServer() {
		if (server != null) {
			startGame();
			return;
		}
		server = new BoggleServer(this);
		server.start();
		startGame.numPlayers.setVisible(true);
		startGame.message.setText("Waiting for others. Click start again to begin.");

		joinGame();
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
		server.startGame();
	}
}
