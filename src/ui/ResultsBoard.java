package ui;

import net.miginfocom.swing.MigLayout;
import server.Results;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ResultsBoard extends JPanel {

	private JPanel scorePanel;
	protected JLabel missedWordsLabel;
	protected JScrollPane playerWordsScroll;
	protected JScrollPane allWorldsListScroll;
	private JList<String> allWordsList;
	protected LetterGrid letterGrid;
	protected JButton nextRoundButton;
	protected JTextField chatTextField;
	protected JTextArea chatArea;
	private JButton resetButton;

	public ResultsBoard(final ResultsBoardController controller) {
		resetButton = new JButton("Reset");
		letterGrid = new LetterGrid();
		nextRoundButton = new JButton("Ready");
		chatTextField = new JTextField();
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		initLayout();
		nextRoundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.nextRoundButtonPressed();
			}
		});
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetButtonPressed();
			}
		});
	}

	public void fillTable(Results results) {
		String[][] resultTableModel = new String[results.getMaxRows()][results.getPlayerResults().size()];
		String[] playerNames = new String[results.getPlayerResults().size()];
		int colIndex = 0;
		for (Map.Entry<String, List<Results.Result>> entry : results.getPlayerResults().entrySet()) {
			int resultIndex = 0;
			playerNames[colIndex] = entry.getKey();
			for (Results.Result result : entry.getValue()) {
				String resultWord = result.getWord();
				if (result.isInvalid()) {
					resultWord = resultWord + " (invalid)";
				} else if (result.isCancelled()) {
					resultWord = "-" + resultWord + "-";
				}
				resultTableModel[resultIndex][colIndex] = resultWord;
				resultIndex++;
			}
			colIndex++;
		}
		JTable wordsTable = new JTable(resultTableModel, playerNames);
		playerWordsScroll.setViewportView(wordsTable);
	}

	private void initLayout() {
		allWordsList = new JList<String>();
		allWordsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allWordsList.setLayoutOrientation(JList.VERTICAL);
		allWorldsListScroll = new JScrollPane(allWordsList);
		allWorldsListScroll.setPreferredSize(new Dimension(300, 400));
		scorePanel = new JPanel(new MigLayout("fillx, gapx 20px"));
		for (int i=0; i < 6; i++) {
			scorePanel.add(new JLabel("_"));
		}
		setLayout(new MigLayout("fillx, hidemode 3"));
		playerWordsScroll = new JScrollPane();
		playerWordsScroll.setSize(300, 300);
		add(letterGrid);
		add(missedWordsLabel = new JLabel("Missed words"), "split, flowy, spany");
		add(allWorldsListScroll, "wrap, growy");
		add(playerWordsScroll, "wrap");
		add(scorePanel, "wrap");
		add(nextRoundButton, "split");
		add(resetButton, "wrap");
		add(chatTextField, "growx, wrap");
		add(new JScrollPane(chatArea), "growx, hmin 100");
	}

	protected void showAllWords(String[] wordList) {
		allWordsList.setListData(wordList);
	}

	protected void fillScorePanel(Map<String, Integer> scores, Map<String, Integer> lastScores) {
		scorePanel.removeAll();
		scorePanel.add(new JLabel("Player"));
		scorePanel.add(new JLabel("This Round"));
		scorePanel.add(new JLabel("Total Score"), "wrap");
		for (String playerName : scores.keySet()) {
			scorePanel.add(new JLabel(playerName + ":"));
			scorePanel.add(new JLabel(lastScores.get(playerName).toString()));
			scorePanel.add(new JLabel(scores.get(playerName).toString()), "wrap");
		}
		validate();
		repaint();
	}

	public JScrollPane getPlayerWordsScroll() {
		return playerWordsScroll;
	}
}
