import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import server.WordChecker;

/**
 * Created by esmith on 4/16/15.
 */
public class WordCheckerTest {

	@Test
	public void testWordCheck() {
		String board = 	"EATA";
		board += 		"TALE";
		board += 		"OILA";
		board += 		"RSTN";
		WordChecker checker = new WordChecker(board);

		assertTrue(checker.checkWord("EAT".toLowerCase()));
		assertTrue(checker.checkWord("TILE".toLowerCase()));
		assertTrue(checker.checkWord("OIL".toLowerCase()));
		assertTrue(checker.checkWord("ROT".toLowerCase()));
		assertTrue(checker.checkWord("RIOS".toLowerCase()));
		assertFalse(checker.checkWord("EAR".toLowerCase()));
		assertFalse(checker.checkWord("ROIR".toLowerCase()));
		assertFalse(checker.checkWord("ROIN".toLowerCase()));
	}
}
