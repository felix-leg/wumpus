package pl.felixspeagel.wumpus;

import pl.felixspeagel.wumpus.game_logic.Cave;
import pl.felixspeagel.wumpus.game_logic.CaveBuilder;
import pl.felixspeagel.wumpus.game_logic.Game;
import pl.felixspeagel.wumpus.gui.GuiMode;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConsoleMain {
    public int arrowsCount;
    public int pitsCount;
    public int batsCount;
    public boolean textMode;

    public static void main(String[] args) {
        Locale userLocale = Locale.getDefault();
        ResourceBundle messages = ResourceBundle.getBundle("messages", userLocale);

        //process arguments
        ConsoleMain main = new ConsoleMain();
        boolean ready;
        try {
            ready = main.processArguments(args, messages);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            ready = false;
        }

        if( !ready ) return;

        CaveBuilder builder = new CaveBuilder();
        builder.setBats(main.batsCount);
        builder.setPits(main.pitsCount);
        builder.setArrows(main.arrowsCount);

        Cave cave = builder.build();
        Game game = new Game(cave);

        if( main.textMode ) {
            TextMode text = new TextMode(game, messages);
            text.gameLoop();
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                GuiMode gui = new GuiMode(game, messages);
                gui.setVisible(true);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public boolean processArguments(String[] args, ResourceBundle messages) throws Exception {
        //set defaults
        arrowsCount = 5;
        pitsCount = 2;
        batsCount = 2;
        textMode = true;

        for(int i=0; i<args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp(messages);
                    return false;
                case "-a":
                    try {
                        i++;
                        arrowsCount = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        throw new Exception(String.format(messages.getString("wrong_option_value"), "-a", args[i]));
                    }
                    break;
                case "-p":
                    try {
                        i++;
                        pitsCount = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        throw new Exception(String.format(messages.getString("wrong_option_value"), "-p", args[i]));
                    }
                    break;
                case "-b":
                    try {
                        i++;
                        batsCount = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        throw new Exception(String.format(messages.getString("wrong_option_value"), "-b", args[i]));
                    }
                    break;
                case "-g":
                    textMode = false;
                    break;
                default:
                    throw new Exception(String.format(messages.getString("wrong_option"), args[i]));
            }
        }

        return true;
    }

    public void printHelp(ResourceBundle messages) {
        System.out.println("wumpus [-h] [-a <int>] [-p <int>] [-b <int>] [-g]\n");
        System.out.println(messages.getString("help_intro"));

        System.out.println();

        System.out.print("-h\t"); System.out.println(messages.getString("help_h_option")); System.out.println();
        System.out.print("-a\t"); System.out.println(messages.getString("help_a_option")); System.out.println();
        System.out.print("-p\t"); System.out.println(messages.getString("help_p_option")); System.out.println();
        System.out.print("-b\t"); System.out.println(messages.getString("help_b_option")); System.out.println();
        System.out.print("-g\t"); System.out.println(messages.getString("help_g_option")); //System.out.println();
    }
}
