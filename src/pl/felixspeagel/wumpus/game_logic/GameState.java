package pl.felixspeagel.wumpus.game_logic;

public sealed class GameState permits HazardEncounteredState, InRoomState, WinState {
}
