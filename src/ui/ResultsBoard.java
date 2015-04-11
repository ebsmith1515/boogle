package ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import server.Results;

public class ResultsBoard extends JPanel {

	private JTable wordsTable;
	private JTable scoreTable;

	public ResultsBoard() {
		scoreTable = new JTable();
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
				resultTableModel[resultIndex][colIndex] = result.getWord();
				resultIndex++;
			}
			colIndex++;
		}
		wordsTable = new JTable(resultTableModel, playerNames);
		initLayout();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		JScrollPane wordsScroll = new JScrollPane(wordsTable);
		wordsScroll.setSize(300, 300);
		add(wordsScroll, BorderLayout.CENTER);
		add(new JScrollPane(scoreTable), BorderLayout.SOUTH);
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
