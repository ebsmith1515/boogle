import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by esmith on 4/13/15.
 */
public class FileStreamTest {

	@Test
	public void test() throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://www.puzzlers.org/pub/wordlists/enable1.txt").openStream()));

		List<String> dictionary = new ArrayList<String>();
		String word;
		while ((word = in.readLine()) != null) {
			dictionary.add(word);
		}
		Assert.assertTrue(dictionary.contains("zebra"));
		Assert.assertTrue(dictionary.contains("baas"));
	}
}
