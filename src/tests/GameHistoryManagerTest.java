package tests;
import game.GameHistoryManager;
import game.UNOModel;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import org.junit.jupiter.api.Test;

public class GameHistoryManagerTest {
    private UNOModel model;
    private GameHistoryManager GHM;

    @BeforeEach
    public void setUp() {
        model = new UNOModel();
        GHM = new GameHistoryManager();
    }
    @Test
    public void testRedo() {
        model.newGame(1, 1);
        int sizeOfHandBefore = model.getCurrentPlayerHand().size();
        model.playCard(model.getCurrentPlayer().getHand().get(0));
        model.redo();
        assertEquals(sizeOfHandBefore, model.getCurrentPlayerHand().size());
    }

    @Test
    public void testUndo() {
        model.newGame(1, 1);
        int sizeOfHandBefore = model.getCurrentPlayerHand().size();
        model.playCard(model.getCurrentPlayer().getHand().get(0));
        model.redo();
        model.undo();
        assertNotEquals(sizeOfHandBefore, model.getCurrentPlayerHand().size());
    }

    @Test
    public void testSaveState() {
        model.newGame(1, 1);
        GHM.saveState(model);
        assertEquals(model, GHM.undo(model));
    }

    @Test
    public void TestresetUndoRedoHistory() {
        model.newGame(1, 1);
        GHM.saveState(model);
        GHM.resetUndoRedoHistory();
        assertEquals(0, GHM.getUndoSize());
        assertEquals(0, GHM.getRedoSize());
    }

    @Test
    public void testRemoveUndo() {
        model.newGame(1, 1);
        GHM.saveState(model);
        GHM.removeUndo();
        assertEquals(0, GHM.getUndoSize());
    }

    @Test
    public void testCanRedo() {
        model.newGame(1, 1);
        GHM.saveState(model);
        GHM.undo(model);
        assertTrue(GHM.canRedo());
    }

    @Test
    public void testCanUndo() {
        model.newGame(1, 1);
        GHM.saveState(model);
        assertTrue(GHM.canUndo());
    }

}
