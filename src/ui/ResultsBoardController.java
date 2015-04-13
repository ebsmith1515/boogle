package ui;

/**
 * Created by esmith on 4/10/15.
 */
public class ResultsBoardController {
	MainController mainController;
	ResultsBoard resultsBoard;

	public ResultsBoardController(MainController mainController) {
		this.mainController = mainController;
		resultsBoard = new ResultsBoard();
	}

	protected void setLetters(String letters) {
		resultsBoard.letterGrid.setLetters(letters);
		resultsBoard.repaint();
	}
}
