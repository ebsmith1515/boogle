package ui;

import client.BoggleClient;
import java.awt.CardLayout;
import java.util.Map;
import java.util.TreeMap;
import server.BoggleServer;
import server.Results;


public class MainController {

	protected MainWindow mainWindow;
	protected GameBoardController gameBoardController;
	protected StartGameController startGameController;
	protected ResultsBoardController resultsBoardController;
	protected BoggleClient client;
	protected BoggleServer server;
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
		gameBoardController.startGame(letters, gameSeconds);
		resultsBoardController.setLetters(gameLetters);
	}

	private void showCard(String name) {
		((CardLayout)mainWindow.getLayout()).show(mainWindow, name);
	}

	public void windowClosing() {
		System.exit(0);
	}

	public void nextRoundButtonPressed() {
		server.nextRound();
	}

	public void showScores(String scoreStr) {
		Map<String, Integer> scoreMap = new TreeMap<String, Integer>();
		String[] scoreSplit = scoreStr.split(BoggleServer.CMD_DELIM);
		for (int i=0; i < scoreSplit.length - 1; i+=2) {
			scoreMap.put(scoreSplit[i], Integer.parseInt(scoreSplit[i+1]));
		}
		resultsBoardController.fillScorePanel(scoreMap);
	}

	public void showAllWords(String[] allWords) {
		resultsBoardController.showAllWords(allWords);
	}

	public boolean isServer() {
		return server != null;
	}
}
