package game;

import java.io.Serializable;
import java.util.Stack;

/**
 * Manages the undo/redo history for the UNO game.
 * Uses deep cloning to save complete game states.
 *
 * @author Magdi Hajjaj
 * @version M5
 */
public class GameHistoryManager implements Serializable {
    private Stack<UNOModel> undoStack;
    private Stack<UNOModel> redoStack;

    /**
     * Constructor for GameHistoryManager
     */
    public GameHistoryManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Saves the current state to the undo stack.
     * Redo stack is cleared when a new state is saved.
     *
     * @param model the current model to save
     */
    public void saveState(UNOModel model) {
        undoStack.push(model.clone());
        redoStack.clear();
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
        redoStack.push(currentModel.clone());
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
        undoStack.push(currentModel.clone());
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
    public void removeUndo() {
        if (undoStack != null && !undoStack.isEmpty()) {
            undoStack.pop();
        }
    }

    public int getUndoSize() {
        return undoStack.size();
    }

    public int getRedoSize() {
        return redoStack.size();
    }
}