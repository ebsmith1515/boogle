package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WordList {
	private static WordList instance;
	private static final String DICTIONARY_URL = "http://www.puzzlers.org/pub/wordlists/enable1.txt";

	protected Map<String, String> dictionary;

	public static WordList getInstance() throws IOException {
		if (instance == null) {
			instance = new WordList();
		}
		return instance;
	}
	private WordList() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(DICTIONARY_URL).openStream()));
		dictionary = new HashMap<String, String>();
		String word;
		while ((word = in.readLine()) != null) {
			dictionary.put(word, "");
		}
	}

	public boolean checkWord(String word) {
		return dictionary.containsKey(word);
	}
}
