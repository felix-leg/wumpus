package pl.felixspeagel.wumpus;

import pl.felixspeagel.wumpus.game_logic.*;

import java.util.ResourceBundle;
import java.util.Scanner;

public class TextMode {
    private final Game game;
    private final ResourceBundle messages;

    private char moveCommand;
    private char shootCommand;
    private char helpCommand;
    private char quitCommand;

    public TextMode(Game newGame, ResourceBundle bundle) {
        game = newGame;
        messages = bundle;

        printWelcome();
        setUpCharCommands();
    }

    private void printWelcome() {
        System.out.println(messages.getString("welcome"));
        System.out.println();
    }

    private void setUpCharCommands() {
        var commandsString = messages.getString("commands_string");
        moveCommand = commandsString.charAt(0);
        shootCommand = commandsString.charAt(1);
        helpCommand = commandsString.charAt(2);
        quitCommand = commandsString.charAt(3);
    }

    public void gameLoop() {
        boolean running = true;
        while(running) {
            GameState state = game.getState();

            if( state instanceof WinState) {
                System.out.println(messages.getString("wumpus_shoot"));
                running = false;
            } else if( state instanceof InRoomState roomState) {
                running = playerInARoom(roomState);
            } else if( state instanceof HazardEncounteredState hazardState ) {
                running = playerInHazard(hazardState);
            }
        }
        System.out.println(messages.getString("bye_bye"));
    }

    private boolean playerInHazard(HazardEncounteredState hazardState) {
        //describe hazard
        switch (hazardState.hazard) {
            case BATS -> System.out.println(messages.getString("got_by_bats"));
            case PIT -> System.out.println(messages.getString("felt_into_pit"));
            case WUMPUS -> System.out.println(messages.getString("wumpus_got_you"));
        }
        System.out.println();
        //can play?
        return ! hazardState.isLethal();
    }

    @SuppressWarnings("RedundantStringFormatCall")
    private boolean playerInARoom(InRoomState state) {
        final var commandPrompt = messages.getString("command_prompt") + "> ";
        final var selectRoomForGOTO = messages.getString("goto_room_select") + " ";
        final var youCantGoThere = messages.getString("cant_go_there");
        final var selectRoomForShoot = messages.getString("shoot_room_select") + " ";
        final var what = messages.getString("unknown_command");
        //
        final var youAreShooting = messages.getString("you_are_shooting");
        final var andMissing = messages.getString("and_missing");
        final var andHitting = messages.getString("and_hitting");
        final var arrowsLeft = messages.getString("arrows_left");
        //
        final var youAreInRoom = messages.getString("you_are_in_room");
        final var canGoTo = messages.getString("you_can_go_to");
        final var iSmellWumpus = "\t" + messages.getString("i_smell_wumpus");
        final var iFeelDraft = "\t" + messages.getString("i_feel_draft");
        final var iHearBats = "\t" + messages.getString("i_hear_bats");

        //print info
        System.out.println(String.format(youAreInRoom, state.roomID));
        System.out.println(String.format(canGoTo, state.canGoto[0], state.canGoto[1], state.canGoto[2]));
        if( state.hasHazardNearby() ) {
            if( state.smellsWumpus ) System.out.println(iSmellWumpus);
            if( state.feelsPit ) System.out.println(iFeelDraft);
            if( state.hearsBats ) System.out.println(iHearBats);
        }
        //print command prompt
        System.out.println();
        System.out.print(commandPrompt);

        //get command
        Scanner scan = new Scanner(System.in);
        char command;
        while( true ) {
            command = scan.next().toUpperCase().charAt(0);
            if( command == moveCommand || command == shootCommand || command == helpCommand || command == quitCommand )
                break;
            System.out.print(what + "\n\n" + commandPrompt);
        }
        scan.reset();

        //respond to command
        if( command == moveCommand ) {
            boolean moved = false;
            int roomChosen;

            while(!moved) {
                System.out.print(selectRoomForGOTO);
                roomChosen = scan.nextInt();
                moved = game.movePlayerTo(roomChosen);
                if(!moved)
                    System.out.println(youCantGoThere);
            }
            return true;
        } else if( command == shootCommand ) {
            //System.out.println("SHOOT");
            int roomChosen;
            boolean hit;

            System.out.print(selectRoomForShoot);
            roomChosen = scan.nextInt();
            System.out.print(youAreShooting);

            hit = game.shootTo(roomChosen);
            if( hit )
                System.out.println(andHitting);
            else {
                System.out.println(andMissing);
                System.out.println(String.format(arrowsLeft, game.getArrowCount()));
            }
            System.out.println();
        } else if( command == helpCommand ) {
            printHelp();
            return true;
        } else //noinspection RedundantIfStatement
            if( command == quitCommand ) {
            return false;
        }

        return true;
    }

    private void printHelp() {
        System.out.println();

        System.out.println("\t\t\t" + messages.getString("ingame_help_intro_title") + "\n");
        System.out.println(messages.getString("ingame_help_intro"));
        System.out.println();

        System.out.println(messages.getString("ingame_help_hazards_title") + ":");
        System.out.println("\t" + messages.getString("ingame_help_hazards").replace("\n", "\n\t"));
        System.out.println();

        System.out.println(messages.getString("ingame_help_wumps_title") + ":");
        System.out.println("\t" + messages.getString("ingame_help_wumps").replace("\n", "\n\t"));
        System.out.println();

        System.out.println(messages.getString("ingame_help_you_title") + ":");
        System.out.println("\t" + String.format(messages.getString("ingame_help_you").replace("\n", "\n\t"), game.getArrowCount()));
        System.out.println();

        System.out.println(messages.getString("ingame_help_warnings_title") + ":");
        System.out.println("\t" + messages.getString("ingame_help_warnings").replace("\n", "\n\t"));
        System.out.println();
    }
}
