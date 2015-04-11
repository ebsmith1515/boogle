package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Results {

	private Map<String, List<Result>> playerResults;
	public static final String RESULTS_DELIM = "%%";
	public static final String PLAYER_DELIM = "&&";

//	public Results(List<String> players) {
//		playerResults = new TreeMap<String, List<Result>>();
//		for (String player : players) {
//			playerResults.put(player, new ArrayList<Result>());
//		}
//	}

	public Results(List<Player> players) {
		playerResults = new TreeMap<String, List<Result>>();
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

	public static class Result {
		private static final String INNER_RESULT_DELIM = "_";
		final private String word;
		private boolean cancelled = false;
		private boolean invalid = false;

		public Result(String word) {
			this.word = word;
		}

		protected static Result fromString(String serStr) {
			List<String> strList = Arrays.asList(serStr.split(INNER_RESULT_DELIM));
			Result result = new Result(strList.get(0));
			strList = strList.subList(1, strList.size());
			if (strList.contains("cancelled")) {
				result.setCancelled(true);
			}
			if (strList.contains("invalid")) {
				result.setInvalid(true);
			}
			return result;
		}

		protected String serialize() {
			String str = word;
			if (cancelled) str += INNER_RESULT_DELIM + "cancelled";
			if (invalid) str += INNER_RESULT_DELIM + "invalid";
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
			return invalid;
		}

		public void setInvalid(boolean invalid) {
			this.invalid = invalid;
		}
	}
}
