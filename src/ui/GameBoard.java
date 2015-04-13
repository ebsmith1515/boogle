package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import static ui.GameBoardController.BOARD_SIZE;

public class GameBoard extends JPanel {

	protected GameBoardController gameBoardController;

	protected LetterGrid letterGrid;
	protected JTextField wordEnter;
	protected JTextArea enteredWordsArea;
	protected JScrollPane enteredWordsScroll;
	protected JLabel timer;

	public GameBoard(GameBoardController gameBoardController) {
		this.gameBoardController = gameBoardController;
		initLayout();
	}

	private void initLayout() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		wordEnter = new JTextField();
		enteredWordsArea = new JTextArea();
		enteredWordsArea.setEditable(false);
		enteredWordsArea.setPreferredSize(new Dimension(150, 600));
		letterGrid = new LetterGrid();
		timer = new JLabel();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel letterGridContainer = new JPanel();
		letterGridContainer.setLayout(new BoxLayout(letterGridContainer, BoxLayout.Y_AXIS));
		letterGridContainer.add(letterGrid);
		Component box = Box.createVerticalGlue();
		letterGridContainer.add(box);
		topPanel.add(letterGridContainer, BorderLayout.WEST);
		topPanel.add(enteredWordsScroll = new JScrollPane(enteredWordsArea), BorderLayout.CENTER);
		add(timer);
		add(topPanel);
		add(wordEnter);
	}
}
