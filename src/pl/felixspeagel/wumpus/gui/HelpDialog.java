package pl.felixspeagel.wumpus.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.WindowEvent;

public class HelpDialog extends JDialog {
    private final JTabbedPane tabPane;
    public HelpDialog(GuiMode gui) {
        super(gui, gui.messages.getString("ingame_help_title"), true);

        tabPane = new JTabbedPane();
        tabPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JButton closeButton = new JButton(gui.messages.getString("ingame_help_close"));
        closeButton.setAlignmentX(0.5f);
        closeButton.addActionListener(actionEvent -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        makeHelpText(gui.messages.getString("ingame_help_intro_title"), gui.messages.getString("ingame_help_intro"));
        makeHelpText(gui.messages.getString("ingame_help_hazards_title"), gui.messages.getString("ingame_help_hazards"));
        makeHelpText(gui.messages.getString("ingame_help_wumps_title"), gui.messages.getString("ingame_help_wumps"));
        makeHelpText(gui.messages.getString("ingame_help_you_title"), gui.messages.getString("ingame_help_you"));
        makeHelpText(gui.messages.getString("ingame_help_warnings_title"), gui.messages.getString("ingame_help_warnings"));

        JPanel dialogPane = new JPanel();
        dialogPane.setLayout(new BoxLayout(dialogPane, BoxLayout.PAGE_AXIS));
        dialogPane.add(tabPane);
        dialogPane.add(closeButton);
        setContentPane(dialogPane);
        pack();
        setLocationRelativeTo(gui);
    }

    private void makeHelpText(String title, String content) {
        JLabel helpLabel = new JLabel("<html>" + content.replace("\n", "<br>") + "</html>");
        helpLabel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        tabPane.addTab(title, helpLabel);
    }
}
