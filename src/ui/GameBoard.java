package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static ui.GameBoardController.BOARD_SIZE;

/**
 * Created by esmith on 4/6/15.
 */
public class GameBoard extends JPanel {

	protected GameBoardController gameBoardController;

	protected JPanel letterGridContainer;
	protected JPanel letterGrid;
	protected JTextField wordEnter;
	protected JTextArea enteredWords;

	public GameBoard(GameBoardController gameBoardController) {
		this.gameBoardController = gameBoardController;
		initLayout();
	}

	private void initLayout() {
		wordEnter = new JTextField();
		enteredWords = new JTextArea();
		enteredWords.setPreferredSize(new Dimension(600, 600));
		letterGrid = new JPanel();
		letterGridContainer = new JPanel();
		letterGrid.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		letterGridContainer.setLayout(new BorderLayout());
		letterGridContainer.setMaximumSize(new Dimension(100, 100));
		letterGridContainer.add(letterGrid, BorderLayout.CENTER);
		add(letterGridContainer);
		add(wordEnter);
		add(enteredWords);
	}

	public GameBoardController getGameBoardController() {
		return gameBoardController;
	}
}
