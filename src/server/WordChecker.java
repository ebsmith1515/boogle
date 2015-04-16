package server;


import java.util.ArrayList;
import java.util.List;

public class WordChecker {

	private char[][] board;

	public WordChecker(String letters, int boardSize) {
		int letterIdx=0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = letters.charAt(letterIdx);
			}
		}
	}

	protected boolean checkWord(String word) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (checkWord(word, new Coord(i, j), new ArrayList<Coord>())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkWord(String word, Coord pos, List<Coord> used) {
		if (pos.row < 0 || pos.row >= board.length) {
			return false;
		}
		if (pos.col < 0 || pos.col >= board[0].length) {
			return false;
		}
		if (used.contains(pos)) {
			return false;
		}
		if (board[pos.row][pos.col] == word.charAt(0)) {
			if (word.length() == 1) {
				return true;
			}
			used.add(pos);

			for (Coord next : nextCoordList(pos)) {
				if (checkWord(word.substring(1), new Coord(next.row, next.col), used)) {
					return true;
				}
			}
		}
		return false;
	}

	private Coord[] nextCoordList(Coord cur) {
		Coord[] coordList = new Coord[8];
		int i=0;
		coordList[i++] = new Coord(cur.row - 1, cur.col - 1);
		coordList[i++] = new Coord(cur.row - 1, cur.col);
		coordList[i++] = new Coord(cur.row - 1, cur.col + 1);

		coordList[i++] = new Coord(cur.row, cur.col - 1);
		coordList[i++] = new Coord(cur.row, cur.col + 1);

		coordList[i++] = new Coord(cur.row + 1, cur.col - 1);
		coordList[i++] = new Coord(cur.row + 1, cur.col);
		coordList[i] = new Coord(cur.row + 1, cur.col + 1);
		return coordList;
	}

	//TODO: handle Qu

	private static class Coord {
		protected int row;
		protected int col;

		private Coord(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Coord coord = (Coord) o;

			if (col != coord.col) return false;
			if (row != coord.row) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = row;
			result = 31 * result + col;
			return result;
		}
	}
}
