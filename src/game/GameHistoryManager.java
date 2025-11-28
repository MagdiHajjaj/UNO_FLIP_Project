package game;

import java.util.Stack;

/**
 * Manages the undo/redo history for the UNO game.
 * Uses deep cloning to save complete game states.
 *
 * @author Magdi Hajjaj
 * @version M4
 */
public class GameHistoryManager {
    private Stack<UNOModel> undoStack;
    private Stack<UNOModel> redoStack;
    private static final int MAX_UNDO_LEVELS = 10;

    /**
     * Constructor for GameHistoryManager
     */
    public GameHistoryManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Saves the current state to the undo stack
     * @param model the current model to save
     */
    public void saveState(UNOModel model) {
        // Limit stack size for performance
        if (undoStack.size() >= MAX_UNDO_LEVELS) {
            undoStack.remove(0); // Remove oldest state
        }
        undoStack.push(model.clone());
        redoStack.clear(); // Clear redo stack when new action is taken
    }

    /**
     * Undoes the last action
     * @param currentModel the current model state
     * @return the previous model state, or null if no undo available
     */
    public UNOModel undo(UNOModel currentModel) {
        if (undoStack.isEmpty()) {
            return null;
        }

        // Save current state to redo stack
        redoStack.push(currentModel.clone());

        // Return previous state
        return undoStack.pop();
    }

    /**
     * Redoes the last undone action
     * @param currentModel the current model state
     * @return the next model state, or null if no redo available
     */
    public UNOModel redo(UNOModel currentModel) {
        if (redoStack.isEmpty()) {
            return null;
        }

        // Save current state to undo stack
        undoStack.push(currentModel.clone());

        // Return next state
        return redoStack.pop();
    }

    /**
     * Checks if undo is available
     * @return true if undo stack is not empty
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Checks if redo is available
     * @return true if redo stack is not empty
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Clears both undo and redo stacks
     */
    public void resetUndoRedoHistory() {
        undoStack.clear();
        redoStack.clear();
    }
}