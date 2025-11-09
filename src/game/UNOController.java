package game;

import javax.swing.*;
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
    public void setWildColor(String wildColor) {
        model.setWildColor(CardColor.valueOf(wildColor));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("DRAW_CARD")) {
            model.drawCard();
        }else if (command.equals("NEXT_PLAYER")) {
            model.nextPlayer();
        }else if (command.equals("PLAY_CARD")) {
            JButton clickedButton = (JButton) e.getSource();
            Card playedCard = (Card) clickedButton.getClientProperty("card");
            model.playCard(playedCard);
        }
    }
}
