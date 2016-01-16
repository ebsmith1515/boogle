package ui;

import client.BoggleClient;
import server.BoggleServer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

public class StartGameController {

	private MainController mainController;

	protected StartGame startGame;

	private static final String PREF_ADDRESS = "ADDRESS";

	public StartGameController(MainController mainController) {
		startGame = new StartGame(this);
		this.mainController = mainController;
		startGame.addressField.setText(getServerAddress());
	}

	private String getServerAddress() {
		return Preferences.userNodeForPackage(StartGameController.class).get(PREF_ADDRESS, "65.60.197.125");
	}

	private void saveServerAddress(String address) {
		Preferences.userNodeForPackage(StartGameController.class).put(PREF_ADDRESS, address);
	}

	public void resetGame() {
		startGame.message.setText("");
		startGame.enterButton.setEnabled(true);
	}

	public void joinGame() {
		startGame.message.setText("");
		mainController.client = new BoggleClient(mainController);
		String ipAddress = startGame.addressField.getText();
		if (getName().equals("")) {
			startGame.message.setText("Please enter a name.");
		} else {
			if (mainController.client.connect(ipAddress)) {
				saveServerAddress(ipAddress);
				mainController.client.start();
				mainController.client.sendName(getName());
				startKeepAliveThread();
			} else {
				startGame.message.setText("Connection refused. Try again.");
			}
		}
	}

	protected void startKeepAliveThread() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mainController.client.pingServer();
			}
		};
		new Timer().scheduleAtFixedRate(task, 0, 2000);
	}

	public String getName() {
		return startGame.nameField.getText().replaceAll(" ", "").replaceAll(BoggleServer.CMD_DELIM, "");
	}
}
