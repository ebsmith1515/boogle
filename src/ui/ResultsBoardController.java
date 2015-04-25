package ui;

import java.util.Map;

public class ResultsBoardController {
	MainController mainController;
	ResultsBoard resultsBoard;

	public ResultsBoardController(MainController mainController) {
		this.mainController = mainController;
		resultsBoard = new ResultsBoard(this);
	}

	protected void setLetters(String letters) {
		resultsBoard.letterGrid.setLetters(letters);
		resultsBoard.repaint();
	}

	protected void fillScorePanel(Map<String, Integer> scores, Map<String, Integer> lastScores) {
		resultsBoard.fillScorePanel(scores, lastScores);
	}

	public void showAllWords(String[] allWords) {
		resultsBoard.showAllWords(allWords);
		resultsBoard.nextRoundButton.setEnabled(true);
	}

	public void nextRoundButtonPressed() {
		mainController.nextRoundButtonPressed();
		resultsBoard.nextRoundButton.setEnabled(false);
	}
}
