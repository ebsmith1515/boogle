package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Timer;

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
		gameBoard.wordEnter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					gameBoard.undoButton.doClick();
					e.consume();
				}
			}
		});
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
					gameBoard.message.setText("Entered '" + fixedWord + "'");
					enteredWords.add(fixedWord);
					setEnteredWordsText();
				}
				gameBoard.wordEnter.setText("");
			}
		});
		gameBoard.undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (enteredWords.size() > 0) {
					String word = enteredWords.get(enteredWords.size() - 1);
					enteredWords.remove(enteredWords.size() - 1);
					setEnteredWordsText();
					gameBoard.message.setText("Removing word: '" + word + "'");
					gameBoard.wordEnter.requestFocusInWindow();
				}
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
		gameBoard.wordEnter.setEnabled(false);
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
		gameBoard.wordEnter.setText("");
		gameBoard.wordEnter.setEnabled(true);
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
