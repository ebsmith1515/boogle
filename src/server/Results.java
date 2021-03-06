package server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Results {

	private final static Logger log = Logger.getLogger(Results.class);

	private Map<String, List<Result>> playerResults;
	public static final String RESULTS_DELIM = "%%";
	public static final String PLAYER_DELIM = "&&";

	public Results(List<Player> players) {
		playerResults = new HashMap<String, List<Result>>();
		for (Player player : players) {
			playerResults.put(player.getPlayerName(), new ArrayList<Result>());
		}
	}

	public Results(String serString) {
		List<String> playersSplit = Arrays.asList(serString.split(PLAYER_DELIM));
		playerResults = new TreeMap<String, List<Result>>();
		for (int i = 0; i < playersSplit.size(); i++) {
			List<String> resultSplit = Arrays.asList(playersSplit.get(i).split(RESULTS_DELIM));
			List<Result> resultList = new ArrayList<Result>();
			playerResults.put(resultSplit.get(0), resultList);
			for (int playerResultsIdx = 1; playerResultsIdx < resultSplit.size(); playerResultsIdx++) {
				resultList.add(Result.fromString(resultSplit.get(playerResultsIdx)));
			}
		}
	}

	private void markDups(List<Result> list1, List<Result> list2) {
		for (Result result1 : list1) {
			for (Result result2 : list2) {
				if (result1.getWord().equals(result2.getWord())) {
					result1.setCancelled(true);
					result2.setCancelled(true);
				}
			}
		}
	}

	private void checkDictionary(WordChecker wordChecker) throws IOException {
		WordList wordList = WordList.getInstance();
		for (String playerName : playerResults.keySet()) {
			List<Result> playerWords = playerResults.get(playerName);
			for (Result result : playerWords) {
				if (!wordList.checkWord(result.word)) {
					result.invalidDictionary = true;
					log.debug(result.word + " not in list");
				} else {
					if (!wordChecker.checkWord(result.word)) {
						log.debug(result.word + " not on board");
						result.invalidBoard = true;
					}
				}
			}
		}
	}

	public void processResults(String gameLetters) {
		WordChecker checker = new WordChecker(gameLetters);
		try {
			checkDictionary(checker);
		} catch (IOException ex) {
			log.debug("Error checking dictionary. Not checking.");
			ex.printStackTrace();
		}

		String[] keySet = playerResults.keySet().toArray(new String[playerResults.size()]);
		for (int i=0; i < keySet.length - 1; i++) {
			for (int j=i+1; j < keySet.length; j++) {
				markDups(playerResults.get(keySet[i]), playerResults.get(keySet[j]));
			}
		}
		for (List<Result> list : playerResults.values()) {
			Collections.sort(list);
		}
	}

	//   Eric||<resultstr>||<resultstr>&&Erin||<resultStr>
	public String serialize() {
		String str = "";
		for (Map.Entry<String, List<Result>> entry : playerResults.entrySet()) {
			str += PLAYER_DELIM + entry.getKey();
			for (Result result : entry.getValue()) {
				str += RESULTS_DELIM + result.serialize();
			}
		}
		return str.substring(2); //remove first &&
	}

	public void addResult(String player, String word) {
		playerResults.get(player).add(new Result(word));
	}

	public Map<String, List<Result>> getPlayerResults() {
		return playerResults;
	}

	public int getMaxRows() {
		int max = 0;
		for (Map.Entry<String, List<Result>> entry : playerResults.entrySet()) {
			max = Math.max(entry.getValue().size(), max);
		}
		return max;
	}

	public static class Result implements Comparable<Result> {
		private static final String INNER_RESULT_DELIM = "_";
		final private String word;
		private boolean cancelled = false;
		private boolean invalidBoard = false;
		private boolean invalidDictionary = false;

		public Result(String word) {
			this.word = word;
		}

		@Override
		public int compareTo(Result o) {
			if (this.isInvalid() && !o.isInvalid()) {
				return 1;
			}
			if (o.isInvalid() && !this.isInvalid()) {
				return -1;
			}
			if (this.cancelled && !o.cancelled) {
				return 1;
			}
			if (o.cancelled && !this.cancelled) {
				return -1;
			}
			return this.word.compareTo(o.word);
		}

		protected static Result fromString(String serStr) {
			List<String> strList = Arrays.asList(serStr.split(INNER_RESULT_DELIM));
			Result result = new Result(strList.get(0));
			strList = strList.subList(1, strList.size());
			if (strList.contains("cancelled")) {
				result.setCancelled(true);
			}
			if (strList.contains("invalid")) {
				result.setInvalidDictionary(true);
			}
			return result;
		}

		protected String serialize() {
			String str = word;
			if (cancelled) str += INNER_RESULT_DELIM + "cancelled";
			if (isInvalid()) str += INNER_RESULT_DELIM + "invalid";
			return str;
		}

		public String getWord() {
			return word;
		}

		public boolean isCancelled() {
			return cancelled;
		}

		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}

		public boolean isInvalid() {
			return invalidBoard || invalidDictionary;
		}

		public boolean isInvalidBoard() {
			return invalidBoard;
		}

		public void setInvalidBoard(boolean invalidBoard) {
			this.invalidBoard = invalidBoard;
		}

		public boolean isInvalidDictionary() {
			return invalidDictionary;
		}

		public void setInvalidDictionary(boolean invalidDictionary) {
			this.invalidDictionary = invalidDictionary;
		}
	}
}
