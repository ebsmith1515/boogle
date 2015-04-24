package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import net.miginfocom.swing.MigLayout;
import server.Results;

public class ResultsBoard extends JPanel {

	private JPanel scorePanel;
	private JScrollPane playerWordsScroll;
	private JList<String> allWordsList;
	protected LetterGrid letterGrid;
	protected JButton nextRoundButton;
	private Map<String, Integer> playerRoundScore;

	public ResultsBoard(final ResultsBoardController controller) {
		letterGrid = new LetterGrid();
		nextRoundButton = new JButton("Next Round");
		initLayout();
		nextRoundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.nextRoundButtonPressed();
			}
		});
	}

	public void fillTable(Results results) {
		playerRoundScore = new TreeMap<String, Integer>();
		String[][] resultTableModel = new String[results.getMaxRows()][results.getPlayerResults().size()];
		String[] playerNames = new String[results.getPlayerResults().size()];
		int colIndex = 0;
		for (Map.Entry<String, List<Results.Result>> entry : results.getPlayerResults().entrySet()) {
			int resultIndex = 0;
			playerNames[colIndex] = entry.getKey();
			int playerScore = 0;
			for (Results.Result result : entry.getValue()) {
				String resultWord = result.getWord();
				if (result.isInvalid()) {
					resultWord = resultWord + " (invalid)";
				} else if (result.isCancelled()) {
					resultWord = "-" + resultWord + "-";
				} else {
					playerScore++;
				}
				resultTableModel[resultIndex][colIndex] = resultWord;
				resultIndex++;
				playerRoundScore.put(entry.getKey(), playerScore);
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
		JScrollPane listScroll = new JScrollPane(allWordsList);
		listScroll.setPreferredSize(new Dimension(300, 400));
		scorePanel = new JPanel(new MigLayout("fillx"));
		for (int i=0; i < 6; i++) {
			scorePanel.add(new JLabel("_"));
		}
		setLayout(new MigLayout("fillx"));
		playerWordsScroll = new JScrollPane();
		playerWordsScroll.setSize(300, 300);
		add(letterGrid);
		add(listScroll, "spany, wrap");
		add(playerWordsScroll, "wrap");
		add(nextRoundButton, "wrap");
		add(scorePanel);
		//add(new JScrollPane(scoreTable), BorderLayout.SOUTH);
	}

	protected void showAllWords(String[] wordList) {
		allWordsList.setListData(wordList);
	}

	protected void fillScorePanel(Map<String, Integer> scores) {
		scorePanel.removeAll();
		for (String playerName : scores.keySet()) {
			int score = playerRoundScore.containsKey(playerName) ? playerRoundScore.get(playerName) : 0;
			scorePanel.add(new JLabel(playerName + ":"));
			scorePanel.add(new JLabel(scores.get(playerName).toString()));
			scorePanel.add(new JLabel("(" + score + ")"), "wrap");
		}
		validate();
		repaint();
	}

	private static class ResultsTableModel extends AbstractTableModel {

		public ResultsTableModel() {

		}

		@Override
		public int getRowCount() {
			return 0;
		}

		@Override
		public int getColumnCount() {
			return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return null;
		}
	}
}
