package pl.felixspeagel.wumpus.gui;

import javax.swing.*;

public class WelcomePane extends JPanel {

    public WelcomePane(GuiMode gui) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        var border = BorderFactory.createEmptyBorder(10,10,10,10);
        JLabel welcomeLabel = new JLabel("<html><center>" + gui.messages.getString("welcome").replace("\n", "<br>") + "</center></html>");
        welcomeLabel.setAlignmentX(0.5f);
        JButton newGameButton = new JButton(gui.messages.getString("new_game_button"));
        newGameButton.setAlignmentX(0.5f);

        welcomeLabel.setBorder(border);
        add(welcomeLabel);
        add(newGameButton);

        newGameButton.addActionListener( actionEvent -> gui.startGame() );
    }
}
