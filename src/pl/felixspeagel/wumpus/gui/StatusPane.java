package pl.felixspeagel.wumpus.gui;

import pl.felixspeagel.wumpus.game_logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class StatusPane extends JPanel {
    private boolean playerDied;
    private final GuiMode gui;

    private final JLabel statusLabel;
    private final JLabel smellWumpusLabel;
    private final JLabel feelDraftLabel;
    private final JLabel hearBatsLabel;

    private final JRadioButton moveCommand;
    private final JRadioButton shootCommand;
    private final JButton firstRoomSelect;
    private final JButton secondRoomSelect;
    private final JButton thirdRoomSelect;

    public StatusPane(GuiMode theGui) {
        super();
        playerDied = false;
        gui = theGui;

        var commandString = gui.messages.getString("commands_string");
        var moveKey = commandString.charAt(0);
        var shootKey = commandString.charAt(1);
        var helpKey = commandString.charAt(2);
        var quitKey = commandString.charAt(3);


        // status/command group
        JPanel scGroup = new JPanel();
        scGroup.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel statusPane = new JPanel();
        c.gridx = c.gridy = 0; c.gridwidth = 1; c.gridheight = 4; c.fill = GridBagConstraints.BOTH;
        scGroup.add(statusPane, c);

        // commands side
        JPanel commandChoosePane = new JPanel();
        commandChoosePane.setLayout(new BoxLayout(commandChoosePane, BoxLayout.LINE_AXIS));
        c.gridx = 1; c.gridy = 0; c.gridwidth = c.gridheight = 1;
        scGroup.add(commandChoosePane, c);

        moveCommand = new JRadioButton(gui.messages.getString("goto_room_select"));
        shootCommand = new JRadioButton(gui.messages.getString("shoot_room_select"));
        commandChoosePane.add(moveCommand);
        commandChoosePane.add(shootCommand);
        ButtonGroup msGroup = new ButtonGroup();
        msGroup.add(moveCommand);
        msGroup.add(shootCommand);
        moveCommand.setSelected(true);

        c.gridx = 1; c.gridy = 1; c.gridheight = c.gridwidth = 1; c.fill = GridBagConstraints.HORIZONTAL;

        firstRoomSelect = new JButton("");
        secondRoomSelect = new JButton("");
        thirdRoomSelect = new JButton("");
        scGroup.add(firstRoomSelect, c); c.gridy++;
        scGroup.add(secondRoomSelect, c); c.gridy++;
        scGroup.add(thirdRoomSelect, c);

        // buttons group
        JPanel btnGroup = new JPanel();
        btnGroup.setLayout(new BoxLayout(btnGroup, BoxLayout.LINE_AXIS));
        btnGroup.setBorder(BorderFactory.createEmptyBorder(5,5,2,5));
        JButton helpButton = new JButton(gui.messages.getString("help_button"));
        JButton quitButton = new JButton(gui.messages.getString("quit_button"));
        btnGroup.add(helpButton);
        btnGroup.add(quitButton);

        //status panel
        statusPane.setBorder(BorderFactory.createEmptyBorder(5,10,5,30));
        statusPane.setLayout(new BoxLayout(statusPane, BoxLayout.PAGE_AXIS));

        statusLabel = new JLabel("STATUS");
        statusPane.add(statusLabel);

        statusPane.add(Box.createVerticalGlue());

        smellWumpusLabel = new JLabel("<html><font color=red>" + gui.messages.getString("i_smell_wumpus") + "</font></html>");
        statusPane.add(smellWumpusLabel);
        smellWumpusLabel.setVisible(false);

        feelDraftLabel = new JLabel("<html><font color=blue>" + gui.messages.getString("i_feel_draft") + "</font></html>");
        statusPane.add(feelDraftLabel);
        feelDraftLabel.setVisible(false);

        hearBatsLabel = new JLabel("<html><font color=green>" + gui.messages.getString("i_hear_bats") + "</font></html>");
        statusPane.add(hearBatsLabel);
        hearBatsLabel.setVisible(false);

        //setting up buttons
        moveCommand.setMnemonic(moveKey);
        shootCommand.setMnemonic(shootKey);
        helpButton.setMnemonic(helpKey);
        quitButton.setMnemonic(quitKey);

        helpButton.addActionListener( actionEvent -> showHelp() );
        quitButton.addActionListener( actionEvent -> quitGame() );

        firstRoomSelect.addActionListener( actionEvent -> gotoOrShoot(0) );
        secondRoomSelect.addActionListener( actionEvent -> gotoOrShoot(1) );
        thirdRoomSelect.addActionListener( actionEvent -> gotoOrShoot(2) );

        // placing groups in the panel
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(scGroup);
        add(btnGroup);
        setVisible(true);
    }

    public boolean updateStatus() {
        var status = gui.game.getState();

        if( status instanceof InRoomState roomState) {
            statusLabel.setText(
                    "<html><b>" +
                    String.format(gui.messages.getString("you_are_in_room"), roomState.roomID) +
                    "<br>" +
                    String.format(gui.messages.getString("arrows_left"), gui.game.getArrowCount()) +
                    "</b></html>"
            );

            smellWumpusLabel.setVisible(roomState.smellsWumpus);
            feelDraftLabel.setVisible(roomState.feelsPit);
            hearBatsLabel.setVisible(roomState.hearsBats);

            firstRoomSelect.setText(String.valueOf(roomState.canGoto[0]));
            secondRoomSelect.setText(String.valueOf(roomState.canGoto[1]));
            thirdRoomSelect.setText(String.valueOf(roomState.canGoto[2]));

            return false;
        } else if(status instanceof HazardEncounteredState hazardState) {
            String messageTitle = gui.messages.getString("hazard_title");
            String messageContent = "";
            switch (hazardState.hazard) {
                case WUMPUS -> messageContent = gui.messages.getString("wumpus_got_you");
                case PIT -> messageContent = gui.messages.getString("felt_into_pit");
                case BATS -> messageContent = gui.messages.getString("got_by_bats");
            }
            JOptionPane.showMessageDialog(getTopLevelAncestor(), messageContent, messageTitle, JOptionPane.WARNING_MESSAGE);

            if( hazardState.isLethal() ) {
                playerDied = true;
                return false;
            } else {
                return true;
            }
        } else if( status instanceof WinState) {

            return false;
        }
        return false;
    }

    public void gotoOrShoot(int roomNo) {
        int roomID;
        switch (roomNo) {
            case 0 -> roomID = Integer.parseInt(firstRoomSelect.getText());
            case 1 -> roomID = Integer.parseInt(secondRoomSelect.getText());
            case 2 -> roomID = Integer.parseInt(thirdRoomSelect.getText());
            default -> throw new RuntimeException("");
        }

        if( moveCommand.isSelected() ) {
            gui.game.movePlayerTo(roomID);
            gui.updateStatus();
        }
        else if( shootCommand.isSelected() ) {
            boolean wumpusHit = gui.game.shootTo(roomID);
            String messageTitle = gui.messages.getString("shoot_room_select") + " " + roomID;
            String messageContent = gui.messages.getString("you_are_shooting");
            if( wumpusHit ) {
                messageContent += gui.messages.getString("and_hitting");
                messageContent += "<br>";
                messageContent += gui.messages.getString("wumpus_shoot").replace("\n", "<br>");
            } else {
                messageContent += gui.messages.getString("and_missing");
            }
            messageContent = "<html>" + messageContent + "</html>";

            JOptionPane.showMessageDialog(getTopLevelAncestor(),messageContent, messageTitle, JOptionPane.PLAIN_MESSAGE);
            if( wumpusHit ) {
                quitGame();
            } else {
                gui.updateStatus();
            }
        }
    }

    public boolean isPlayerDied() {
        return playerDied;
    }

    private void showHelp() {
        HelpDialog helpDialog = new HelpDialog(gui);
        helpDialog.setVisible(true);
    }
    private void quitGame() {
        var window = (GuiMode) getTopLevelAncestor();
        window.dispatchEvent( new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
}
