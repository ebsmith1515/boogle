package ui;

import client.BoggleClient;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.BoggleServer;
import server.Player;

public class StartGameController {

	@FXML
	private Button startButton;
	@FXML
	private Button joinButton;
	@FXML
	private Label numPlayers;
	@FXML
	private TextField ipfield;

	private BoggleServer server;
	private BoggleClient client;
	private MainController mainController;

	@FXML
	private MessageController messageController;

	public void joinGame() {
		client = new BoggleClient(mainController);
		String ipAddress = null;
		if (!ipfield.getText().isEmpty()) {
			ipAddress = ipfield.getText();
		}
		if (client.connect(ipAddress)) {
			joinButton.setDisable(true);
			startButton.setDisable(true);
			client.start();
		} else {
			messageController.message("Connection refused. Try again.", MessageController.Duration.SHORT);
		}
	}

	public void initServer() {
		if (server != null) {
			startGame();
			return;
		}
		server = new BoggleServer();
		server.start();
		numPlayers.setVisible(true);
		messageController.message("Waiting for others. Click start again to begin.", MessageController.Duration.LONG);

		server.getPlayers().addListener(new ListChangeListener<Player>() {
			@Override
			public void onChanged(Change<? extends Player> c) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						numPlayers.setText("Number of Players: " + server.getPlayers().size());
					}
				});
			}
		});
		joinGame();
		startButton.setDisable(false);
	}

	private void startGame() {
		messageController.message("Starting game...", MessageController.Duration.SHORT);
		server.startGame();
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}
