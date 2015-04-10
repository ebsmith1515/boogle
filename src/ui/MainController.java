package ui;

import client.BoggleClient;
import java.awt.CardLayout;


public class MainController {

	protected MainWindow mainWindow;
	protected GameBoardController gameBoardController;
	protected StartGameController startGameController;
	protected BoggleClient client;

	public MainController() {
		gameBoardController = new GameBoardController(this);
		startGameController = new StartGameController(this);
		mainWindow = new MainWindow(this);
	}

	public void showGameBoard(int gameSeconds) {
		((CardLayout)mainWindow.getLayout()).show(mainWindow, "GameBoard");
		gameBoardController.startTimer(gameSeconds);
	}
}
