package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import static ui.GameBoardController.BOARD_SIZE;

public class LetterGrid extends JPanel {
	private static final int BOARD_SIZE_PIXEL = 50;

	public LetterGrid() {
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		setMinimumSize(new Dimension(BOARD_SIZE_PIXEL * 4, BOARD_SIZE_PIXEL * 4));
		setPreferredSize(new Dimension(BOARD_SIZE_PIXEL * 4, BOARD_SIZE_PIXEL * 4));
		setMaximumSize(new Dimension(BOARD_SIZE_PIXEL * 4, BOARD_SIZE_PIXEL * 4));
		setLetters("AAAAAAAAAAAAAAAA");
	}

	public void setLetters(String letters) {
		removeAll();
		int letterIdx = 0;
		for (int i=0; i < BOARD_SIZE; i++) {
			for (int j=0; j < BOARD_SIZE; j++) {
				add(letterLabel(letters.substring(letterIdx, letterIdx+1)), SwingConstants.CENTER);
				letterIdx++;
			}
		}
	}

	private JLabel letterLabel(String letter) {
		JLabel label = new JLabel(letter);
		label.setFont(new Font("name", Font.PLAIN, 37));
		if (letter.equals("Q")) {
			label.setText("Qu");
			label.setFont(new Font("name", Font.PLAIN, 24));
		}

		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		Border lineBorder = new LineBorder(Color.BLACK);
		label.setBorder(lineBorder);
		label.setPreferredSize(new Dimension(BOARD_SIZE_PIXEL, BOARD_SIZE_PIXEL));
		label.setMaximumSize(new Dimension(BOARD_SIZE_PIXEL, BOARD_SIZE_PIXEL));
		return label;
	}
}
