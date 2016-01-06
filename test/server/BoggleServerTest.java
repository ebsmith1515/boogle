package server;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import server.BoggleServer;
import server.Player;

/**
 * Created by esmith on 4/11/15.
 */
public class BoggleServerTest {

	@Test
	public void testTwoPlayers() {
		BoggleServer server = new BoggleServer();
		Player player1 = new Player(1);
		Player player2 = new Player(2);
		server.getPlayers().add(player1);
		server.getPlayers().add(player2);

		player1.handleWords("WORDS hello world");
		player2.handleWords("WORDS what up");

		server.checkEnd();
		assertEquals(2, server.getResults().getPlayerResults().keySet().size());
		assertEquals(2, server.getResults().getPlayerResults().get("Player1").size());
	}

	@Test
	public void testFirstPlayer() {
		BoggleServer server = new BoggleServer();
		Player player1 = new Player(1);
		server.getPlayers().add(player1);
		server.sendResults();
	}
}
