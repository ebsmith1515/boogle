package ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Dimension;

public class GameBoard extends JPanel {

	protected GameBoardController gameBoardController;

	protected LetterGrid letterGrid;
	protected JTextField wordEnter;
	protected JTextArea enteredWordsArea;
	protected JScrollPane enteredWordsScroll;
	protected JLabel timer;
	protected JLabel message;
	protected JButton undoButton;

	public GameBoard(GameBoardController gameBoardController) {
		this.gameBoardController = gameBoardController;
		initComponents();
		initLayouts();
	}

	private void initComponents() {
		wordEnter = new JTextField();
		enteredWordsArea = new JTextArea();
		enteredWordsArea.setEditable(false);
		enteredWordsArea.setPreferredSize(new Dimension(150, 600));
		letterGrid = new LetterGrid();
		timer = new JLabel();
		message = new JLabel();
		undoButton = new JButton("Undo (Spacebar)");
	}

	private void initLayouts() {
		setLayout(new MigLayout("fill"));
		add(timer, "split5, center, flowy");
		add(letterGrid, "center");
		add(message, "center");
		add(wordEnter, "center, wmin 200");
		add(undoButton, "center");
		add(enteredWordsScroll = new JScrollPane(enteredWordsArea), "growx, wrap");
	}
}
