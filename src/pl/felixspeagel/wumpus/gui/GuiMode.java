package pl.felixspeagel.wumpus.gui;

import pl.felixspeagel.wumpus.game_logic.Game;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class GuiMode extends JFrame {
    final Game game;
    final ResourceBundle messages;

    @SuppressWarnings("FieldCanBeLocal")
    private final WelcomePane welcome_pane;
    private final StatusPane status_pane;

    public GuiMode(Game newGame, ResourceBundle bundle) {
        super(bundle.getString("game_title"));
        game = newGame;
        messages = bundle;

        welcome_pane = new WelcomePane(this);
        status_pane = new StatusPane(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(welcome_pane);
        pack();
        setLocationRelativeTo(null);
    }

    public void startGame() {
        setContentPane(status_pane);
        repaint();
        pack();
        updateStatus();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void updateStatus() {
        while(status_pane.updateStatus()) {}
        pack();

        if( status_pane.isPlayerDied() ) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
