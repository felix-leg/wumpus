package pl.felixspeagel.wumpus.game_logic;

public final class HazardEncounteredState extends GameState {
    public enum HazardType {
        BATS,
        PIT,
        WUMPUS
    }
    public final HazardType hazard;

    public HazardEncounteredState(HazardType aHazard) {
        hazard = aHazard;
    }

    public boolean isLethal() {
        return hazard != HazardType.BATS;
    }
}
