package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import net.miginfocom.swing.MigLayout;
import server.Results;

public class ResultsBoard extends JPanel {

	private JPanel scorePanel;
	private JTable wordsTable;
	private JScrollPane wordsScroll;
	protected LetterGrid letterGrid;
	private JButton nextRoundButton;

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

//	private Results makeResults() {
//		List<String> players = new ArrayList<String>();
//		players.add("Eric");
//		players.add("Erin");
//		Results results = new Results(players);
//		results.addResult("Eric", "hello");
//		results.addResult("Erin", "hello2");
//		return results;
//	}

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
		wordsTable = new JTable(resultTableModel, playerNames);
		wordsScroll.setViewportView(wordsTable);
	}

	private void initLayout() {
		scorePanel = new JPanel(new MigLayout("fillx"));
		for (int i=0; i < 6; i++) {
			scorePanel.add(new JLabel("_"));
		}
		setLayout(new MigLayout("fillx"));
		wordsScroll = new JScrollPane();
		wordsScroll.setSize(300, 300);
		add(letterGrid, "wrap");
		add(wordsScroll, "wrap");
		add(nextRoundButton, "wrap");
		add(scorePanel);
		//add(new JScrollPane(scoreTable), BorderLayout.SOUTH);
	}

	protected void fillScorePanel(Map<String, Integer> scores) {
		scorePanel.removeAll();
		for (String playerName : scores.keySet()) {
			scorePanel.add(new JLabel(playerName + ":"));
			scorePanel.add(new JLabel(scores.get(playerName).toString()), "wrap");
		}
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
