package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class GameBoardController {

	protected GameBoard gameBoard;
	private List<String> enteredWords;
	public static final int BOARD_SIZE = 4;
	private MainController mainController;
	private long timeLeft;

	public GameBoardController(MainController mainController) {
		this.mainController = mainController;
		gameBoard = new GameBoard(this);
		enteredWords = new ArrayList<String>();
		addLetters();
		addListeners();
	}

	private void addListeners() {
		gameBoard.wordEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!enteredWords.contains(gameBoard.wordEnter.getText())) {
					enteredWords.add(gameBoard.wordEnter.getText());
					setEnteredWordsText();
				}
				gameBoard.wordEnter.setText("");
			}
		});
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
		for (String word : enteredWords) {
			gameBoard.enteredWordsArea.append(word + "\n");
		}
	}

	private void addLetters() {
		for (int i=0; i < BOARD_SIZE; i++) {
			for (int j=0; j < BOARD_SIZE; j++) {
				gameBoard.letterGrid.add(letterLabel(getLetter()));
			}
		}
	}

	private JLabel letterLabel(String letter) {
		JLabel label = new JLabel(letter);
		label.setBorder(new LineBorder(Color.BLACK));
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	private String getLetter() {
		return "A";
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
