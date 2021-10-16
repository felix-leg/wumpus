package pl.felixspeagel.wumpus.game_logic;

public final class InRoomState extends GameState {
    public final int roomID;
    public final int[] canGoto;
    public final boolean smellsWumpus;
    public final boolean feelsPit;
    public final boolean hearsBats;

    public InRoomState(int id, int[] tunnels, boolean wumpus, boolean pit, boolean bats) {
        roomID = id;
        canGoto = tunnels;
        smellsWumpus = wumpus;
        feelsPit = pit;
        hearsBats = bats;
    }

    public boolean hasHazardNearby() {
        return smellsWumpus || feelsPit || hearsBats;
    }
}
