package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UNOController implements ActionListener {
    UNOModel model;
    UNOView view;
    public UNOController(UNOModel model, UNOView view) {
        this.model = model;
        this.view = view;
    }
    public void setNumPlayers(int numPlayers) {
        model.newGame(numPlayers);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
