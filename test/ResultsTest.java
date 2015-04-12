import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import server.Player;
import server.Results;


public class ResultsTest {

	@Test
	public void testSerialize() {
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(1));
		playerList.add(new Player(2));
		Results results = new Results(playerList);
		results.addResult("Player1", "hello");
		results.addResult("Player2", "hello");
		results.addResult("Player1", "world");
		results.addResult("Player2", "world");

		String serString = results.serialize();

		Results deserialized = new Results(serString);

		assertEquals(results.getPlayerResults().keySet(), deserialized.getPlayerResults().keySet());
		assertEquals(results.getPlayerResults().get("Player1").get(0).getWord(), deserialized.getPlayerResults().get("Player1").get(0).getWord());
		assertEquals(results.getPlayerResults().get("Player2").get(0).getWord(), deserialized.getPlayerResults().get("Player2").get(0).getWord());
		assertEquals(results.getPlayerResults().get("Player1").get(1).getWord(), deserialized.getPlayerResults().get("Player1").get(1).getWord());
		assertEquals(results.getPlayerResults().get("Player2").get(1).getWord(), deserialized.getPlayerResults().get("Player2").get(1).getWord());
	}

	@Test
	public void testSort() {
		List<Results.Result> resultsList = new ArrayList<Results.Result>();
		resultsList.add(new Results.Result("asdf"));
		resultsList.add(new Results.Result("world"));
		resultsList.add(new Results.Result("hello"));

		resultsList.get(0).setCancelled(true);

		Collections.sort(resultsList);
		System.out.print(resultsList.size());
	}

	@Test
	public void testMarkDups() {
		List<Player> players = new ArrayList<Player>();
		players.add(new Player(1));
		players.add(new Player(2));
		players.add(new Player(3));
		Results results = new Results(players);
		results.addResult("Player1", "hello");
		results.addResult("Player2", "hello");
		results.addResult("Player3", "hello");
		results.addResult("Player1", "world");
		results.addResult("Player2", "wurld");
		results.addResult("Player3", "wirld");

		results.processResults();
		results.getPlayerResults().get("Player1");
	}
}
