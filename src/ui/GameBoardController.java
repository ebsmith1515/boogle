package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GameBoardController {

	protected GameBoard gameBoard;
	private List<String> enteredWords;
	public static final int BOARD_SIZE = 4;
	private MainController mainController;
	private long timeLeft;
	private String gameLetters;

	public GameBoardController(MainController mainController) {
		this.mainController = mainController;
		gameBoard = new GameBoard(this);
		enteredWords = new ArrayList<String>();
		addListeners();
	}

	private void addListeners() {
		gameBoard.wordEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fixedWord = gameBoard.wordEnter.getText().toLowerCase();
				fixedWord = fixedWord.replaceAll(" ", "");
				if (!enteredWords.contains(fixedWord) && isAllLettersOnBoard(fixedWord)) {
					enteredWords.add(fixedWord);
					setEnteredWordsText();
					gameBoard.wordEnter.setText("");
				}
			}
		});
	}

	private boolean isAllLettersOnBoard(String word) {
		//TODO: make sure Qu works here
		boolean allGood = true;
		for (int i=0; i < word.length(); i++) {
			allGood &= gameLetters.contains(word.substring(i, i+1).toUpperCase());
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

	private JLabel letterLabel(String letter) {
		JLabel label = new JLabel(letter);
		label.setFont(new Font("name", Font.PLAIN, 37));
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		Border lineBorder = new LineBorder(Color.BLACK);
		label.setBorder(lineBorder);
		label.setPreferredSize(new Dimension(50, 50));
		label.setMaximumSize(new Dimension(50, 50));
		return label;
	}

	public void setLetters(String letters) {
		this.gameLetters = letters;
		int letterIdx = 0;
		for (int i=0; i < BOARD_SIZE; i++) {
			for (int j=0; j < BOARD_SIZE; j++) {
				gameBoard.letterGrid.add(letterLabel(letters.substring(letterIdx, letterIdx+1)), SwingConstants.CENTER);
				letterIdx++;
			}
		}
		gameBoard.wordEnter.requestFocusInWindow();
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
