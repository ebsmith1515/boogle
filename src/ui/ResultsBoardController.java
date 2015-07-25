package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ResultsBoardController {
	MainController mainController;
	ResultsBoard resultsBoard;

	public ResultsBoardController(final MainController mainController) {
		this.mainController = mainController;
		resultsBoard = new ResultsBoard(this);
		resultsBoard.chatTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (resultsBoard.chatTextField.getText().length() > 0) {
					mainController.client.sendChat(resultsBoard.chatTextField.getText());
					resultsBoard.chatTextField.setText("");
				}
			}
		});
		resultsBoard.getPlayerWordsScroll().setToolTipText("<html>Words surrounded by hyphens are cancelled due to another player" +
				"<br />getting the same word. Invalid words are shown at the bottom. Words that are not in the dictionary are worth" +
				"<br />-1 point.</html>");
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
		resultsBoard.chatArea.setText("");
		resultsBoard.chatTextField.requestFocusInWindow();
	}

	public void nextRoundButtonPressed() {
		mainController.nextRoundButtonPressed();
		resultsBoard.nextRoundButton.setEnabled(false);
	}

	public void addChatMessage(String playerName, String message) {
		resultsBoard.chatArea.setText(playerName + ": " + message + "\n" + resultsBoard.chatArea.getText());
	}

	public void resetButtonPressed() {
		mainController.reset();
	}
}
