package server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BoardGenerator implements Runnable {

	private static final Logger log = Logger.getLogger(BoardGenerator.class);

	private static final int MIN_WORDS_ON_BOARD = 50;

	protected String gameLetters;
	private WordChecker wordChecker;
	private int wordsOnBoardCount;
	protected HashMap<Integer, List<String>> wordsOnBoard;
	protected boolean done = false;

	@Override
	public void run() {
		generateBoard();
	}

	public void generateBoard() {
		done = false;
		while (!done) {
			log.debug("Getting letters");
			gameLetters = new LetterFactory().getLetterList();
			wordChecker = new WordChecker(gameLetters);
			findAllWords();
			done = wordsOnBoardCount > MIN_WORDS_ON_BOARD;
			log.debug("Found " + wordsOnBoardCount + "words");
		}
	}

	//TODO: option for min words on UI
	private void findAllWords() {
		wordsOnBoardCount = 0;
		long startTime = System.currentTimeMillis();
		wordsOnBoard = new HashMap<Integer, List<String>>();
		try {
			WordList wordList = WordList.getInstance();
			for (String word : wordList.dictionary.keySet()) {
				if (word.length() > 2 && wordChecker.checkWord(word)) {
					putInWordsOnBoard(word.length(), word);
					wordsOnBoardCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long seconds = (System.currentTimeMillis() - startTime) / 1000;
		log.debug("Ran findAllWords in " + seconds + " seconds.");
	}

	private void putInWordsOnBoard(int num, String word) {
		if (!wordsOnBoard.containsKey(num)) {
			wordsOnBoard.put(num, new ArrayList<String>());
		}
		wordsOnBoard.get(num).add(word);
	}

}
