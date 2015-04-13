package server;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LetterFactory {

	private static List<String[]> letterMatrix = new ArrayList<String[]>();
	static {
		newCube("VEZADN");
		newCube("VETING");
		newCube("ESACRL");
		newCube("KUTODN");

		newCube("AHOSRM");
		newCube("EEIYHF");
		newCube("IXOBRF");
		newCube("LYBTAI");

		newCube("CATIOA");
		newCube("NIPEHS");
		newCube("EDONSW");
		newCube("RUGILW");

		newCube("MCDPAE");
		newCube("AMOQBJ");
		newCube("PSLTEU");
		newCube("KELUYG");
	}

	private static void newCube(String letters) {
		String[] list = new String[6];
		for (int i=0; i < list.length; i++) {
			list[i] = String.valueOf(letters.charAt(i));
		}
		letterMatrix.add(list);
	}

	public String getLetterList() {
		String gameLetterList = "";
		Random rnd = new SecureRandom();
		Collections.shuffle(letterMatrix, rnd);
		for (String[] cube : letterMatrix) {
			int idx = rnd.nextInt(6);
			gameLetterList += cube[idx];
		}
		return gameLetterList;
	}
}
