package ui;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameBoard extends JPanel {

	protected GameBoardController gameBoardController;

	protected LetterGrid letterGrid;
	protected JTextField wordEnter;
	protected JTextArea enteredWordsArea;
	protected JScrollPane enteredWordsScroll;
	protected JLabel timer;
	protected JLabel message;

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
	}

	private void initLayouts() {
		setLayout(new MigLayout("fill"));
		add(timer, "split3, center, flowy");
		add(letterGrid, "center");
		add(message, "center");
		add(enteredWordsScroll = new JScrollPane(enteredWordsArea), "growx, wrap");
		add(wordEnter, "growx, span");
	}
}
