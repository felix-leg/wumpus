package pl.felixspeagel.wumpus.game_logic;

import java.util.Random;
import java.util.Arrays;

public class Game {

    private final Cave cave;
    private boolean wumpusKilled;

    public Game(Cave newCave) {
        cave = newCave;
        wumpusKilled = false;
    }

    public GameState getState() {
        //check for winning state
        if( wumpusKilled ) {
            return new WinState();
        }
        //check for hazards
        Room playersRoom = cave.getRoomWithID(cave.playerPos);

        if( playersRoom.hasBats ) {
            Random random = new Random();
            int movedTo = random.nextInt(1, 21);
            movePlayerTo(movedTo, true);
            return new HazardEncounteredState(HazardEncounteredState.HazardType.BATS);
        }
        if( playersRoom.hasPit ) {
            return new HazardEncounteredState(HazardEncounteredState.HazardType.PIT);
        }
        if( playersRoom.hasWumpus || cave.playerArrows < 1 ) {
            return new HazardEncounteredState(HazardEncounteredState.HazardType.WUMPUS);
        }

        //no hazard - return possible movements
        int[] tunnels = playersRoom.connections;
        Arrays.sort(tunnels);
        //::detect hazards nearby
        boolean smellsWumpus = false;
        boolean hearsBats = false;
        boolean feelsPit = false;

        for (int tunnel : tunnels) {
            Room connectedRoom = cave.getRoomWithID(tunnel);

            if (connectedRoom.hasBats) hearsBats = true;
            if (connectedRoom.hasPit) feelsPit = true;
            if (connectedRoom.hasWumpus) smellsWumpus = true;
        }
        //::return state
        return new InRoomState(cave.playerPos, tunnels, smellsWumpus, feelsPit, hearsBats);
    }

    public boolean movePlayerTo(int roomID) {
        return movePlayerTo(roomID, false);
    }

    private boolean movePlayerTo(int roomID, boolean force) {
        if(!force) {
            if( ! cave.getRoomWithID(cave.playerPos).isConnectedTo(roomID) ) {
                return false;
            }
        }
        cave.playerPos = roomID;
        return true;
    }

    public boolean shootTo(int roomID) {
        if( cave.playerArrows < 1 ) return false;
        cave.playerArrows -= 1;
        //shoot arrow
        boolean missed;
        if( ! cave.getRoomWithID(cave.playerPos).isConnectedTo(roomID) ) {
            missed = true;
        } else {
            missed = ! cave.getRoomWithID(roomID).hasWumpus;
        }
        //move wumpus if missed
        if( missed ) {
            Random random = new Random();
            int roomIDWithWumpus = cave.getRoomIDWithWumpus();
            Room roomWithWumpus = cave.getRoomWithID(roomIDWithWumpus);
            roomWithWumpus.hasWumpus = false;

            int[] possibleMoves = new int[4];
            possibleMoves[0] = roomIDWithWumpus;
            possibleMoves[1] = roomWithWumpus.connections[0];
            possibleMoves[2] = roomWithWumpus.connections[1];
            possibleMoves[3] = roomWithWumpus.connections[2];

            roomIDWithWumpus = possibleMoves[random.nextInt(4)];
            roomWithWumpus = cave.getRoomWithID(roomIDWithWumpus);
            roomWithWumpus.hasWumpus = true;
        } else {
            wumpusKilled = true;
        }

        return !missed;
    }

    public int getArrowCount() {
        return cave.playerArrows;
    }
}
