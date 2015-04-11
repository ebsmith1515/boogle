package ui;

import client.BoggleClient;
import java.awt.CardLayout;
import server.Results;


public class MainController {

	protected MainWindow mainWindow;
	protected GameBoardController gameBoardController;
	protected StartGameController startGameController;
	protected ResultsBoardController resultsBoardController;
	protected BoggleClient client;

	public MainController() {
		gameBoardController = new GameBoardController(this);
		startGameController = new StartGameController(this);
		resultsBoardController = new ResultsBoardController(this);
		mainWindow = new MainWindow(this);
	}

	public void showResults(Results results) {
		showCard("ResultsBoard");
		resultsBoardController.resultsBoard.fillTable(results);
	}

	public void showGameBoard(int gameSeconds) {
		showCard("GameBoard");
		gameBoardController.startTimer(gameSeconds);
	}

	private void showCard(String name) {
		((CardLayout)mainWindow.getLayout()).show(mainWindow, name);
	}

	public void windowClosing() {
		System.exit(0);
	}
}
