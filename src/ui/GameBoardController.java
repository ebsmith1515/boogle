package ui;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class GameBoardController {

	protected GameBoard gameBoard;

	public static final int BOARD_SIZE = 4;

	public GameBoardController() {
		gameBoard = new GameBoard(this);
		addLetters();
	}

	private void addWord(String word) {
		gameBoard.enteredWords.append(word + "\n");
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
}
