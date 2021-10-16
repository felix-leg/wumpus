package pl.felixspeagel.wumpus.game_logic;

public class Room {
    public final int id;
    public int[] connections;
    public boolean hasBats;
    public boolean hasPit;
    public boolean hasWumpus;

    public Room(int roomID) {
        id = roomID;
        hasBats = false;
        hasPit = false;
        hasWumpus = false;
        connections = new int[3];
        //connections[0] = connections[1] = connections[2] = 0;
    }

    public void addConnection(Room anotherRoom) {
        int newConnection = anotherRoom.id;
        if( isConnectedTo(newConnection) ) return;
        if( connections[0] == 0 ) {
            connections[0] = newConnection;
            anotherRoom.addConnection(this);
            return;
        }
        if( connections[1] == 0 ) {
            connections[1] = newConnection;
            anotherRoom.addConnection(this);
            return;
        }
        if( connections[2] == 0 ) {
            connections[2] = newConnection;
            anotherRoom.addConnection(this);
            return;
        }
        throw new RuntimeException("A room can only have got 3 connections.");
    }

    public boolean isConnectedTo(int anotherRoomID) {
        return
                (connections[0] == anotherRoomID) ||
                (connections[1] == anotherRoomID) ||
                (connections[2] == anotherRoomID);
    }
}
