import game.*;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AITest {
    private UNOModel game;
    private Player human;
    private ComputerPlayer computer;
    @BeforeEach
    public void setUp()
    {
        game = new UNOModel();
        game.newGame(1,1);
        ArrayList<Player> players = game.getPlayers();
        human = players.get(0);
        computer = (ComputerPlayer) players.get(1);
        game.nextPlayer();
    }

    @Test
    void testComputerChoice(){
        Card card = new Card(game.getTopCard().getColor(), CardType.ONE);
        computer.clearHand();
        computer.addCard(card);

        Card choose = game.computerCardChoice();
        assertNotNull(choose);
        assertEquals(card, choose);
    }
}
