package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

public class GameBoardController {

	protected GameBoard gameBoard;
	private List<String> enteredWords;
	public static final int BOARD_SIZE = 4;
	private MainController mainController;
	private long timeLeft;

	public GameBoardController(MainController mainController) {
		this.mainController = mainController;
		gameBoard = new GameBoard(this);
		addListeners();
	}

	private void addListeners() {
		gameBoard.wordEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameBoard.message.setText(" ");
				String fixedWord = gameBoard.wordEnter.getText().toLowerCase();
				fixedWord = fixedWord.replaceAll(" ", "");
				if (fixedWord.length() < 3) {
					gameBoard.message.setText("Not 3 letters");
				} else if (enteredWords.contains(fixedWord)) {
					gameBoard.message.setText(fixedWord + " already entered");
				} else if (!isAllLettersOnBoard(fixedWord)) {
					gameBoard.message.setText(fixedWord + " is not on the board");
				} else {
					enteredWords.add(fixedWord);
					setEnteredWordsText();
				}
				gameBoard.wordEnter.setText("");
			}
		});
	}

	private boolean isAllLettersOnBoard(String word) {
		word = word.replace("qu", "q");
		boolean allGood = true;
		for (int i=0; i < word.length(); i++) {
			allGood &= mainController.gameLetters.contains(word.substring(i, i+1).toUpperCase());
		}
		return allGood;
	}

	protected void endGame() {
		mainController.client.sendWords(enteredWords);
	}

	protected void startTimer(int gameSeconds) {
		final long period = 500;
		timeLeft = gameSeconds * 1000;
		gameBoard.timer.setText(Integer.toString(gameSeconds));
		new Timer().schedule(new BoggleTimerTask(period), period, period);
	}

	private void setEnteredWordsText() {
		gameBoard.enteredWordsArea.setText("");
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(enteredWords);
		Collections.reverse(list);
		for (String word : list) {
			gameBoard.enteredWordsArea.append(word + "\n");
		}
		gameBoard.enteredWordsArea.setCaretPosition(0);
	}

	public void setGameLetters(String gameLetters) {
		mainController.gameLetters = gameLetters;
		gameBoard.letterGrid.setLetters(gameLetters);
		gameBoard.wordEnter.requestFocusInWindow();
	}

	public void startGame(String letters, int gameSeconds) {
		gameBoard.message.setText(" ");
		enteredWords = new ArrayList<String>();
		gameBoard.enteredWordsArea.setText("");
		setGameLetters(letters);
		startTimer(gameSeconds);
	}


	private class BoggleTimerTask extends TimerTask {

		long period;

		private BoggleTimerTask(long period) {
			this.period = period;
		}

		@Override
		public void run() {
			timeLeft -= period;
			if (timeLeft <=0) {
				gameBoard.timer.setText("Finished!");
				endGame();
				this.cancel();
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						gameBoard.timer.setText(Float.toString(timeLeft / 1000f));
					}
				});
			}
		}
	}
}
