package game;

public class UNOController {
    UNOModel model;
    UNOView view;
    public UNOController(UNOModel model, UNOView view) {
        this.model = model;
        this.view = view;
    }
    public void setNumPlayers(int numPlayers) {
        model.newGame(numPlayers);
    }
}
