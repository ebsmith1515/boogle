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
	protected String gameLetters;

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

	public void showGameBoard(int gameSeconds, String letters) {
		showCard("GameBoard");
		gameLetters = letters;
		gameBoardController.setGameLetters(letters);
		gameBoardController.startTimer(gameSeconds);
		resultsBoardController.setLetters(gameLetters);
	}

	private void showCard(String name) {
		((CardLayout)mainWindow.getLayout()).show(mainWindow, name);
	}

	public void windowClosing() {
		System.exit(0);
	}
}
