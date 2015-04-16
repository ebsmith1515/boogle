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

	protected void fillScorePanel(Map<String, Integer> scores) {
		resultsBoard.fillScorePanel(scores);
		if (!mainController.isServer()) {
			resultsBoard.nextRoundButton.setVisible(false);
		}
	}

	public void nextRoundButtonPressed() {
		mainController.nextRoundButtonPressed();
	}
}
