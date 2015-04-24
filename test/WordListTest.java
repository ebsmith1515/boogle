import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;
import server.WordList;

/**
 * Created by esmith on 4/19/15.
 */
public class WordListTest {

	@Test
	public void testFeed() throws IOException {
		Assert.assertTrue(WordList.getInstance().checkWord("FEED"));
	}
}
