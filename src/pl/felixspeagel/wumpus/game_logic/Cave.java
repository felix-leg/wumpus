package pl.felixspeagel.wumpus.game_logic;

public class Cave {

    public Room[] rooms;
    public int playerPos;
    public int playerArrows;

    public Cave(int arrowNum) {
        rooms = new Room[20];
        playerPos = 0;
        playerArrows = arrowNum;
    }

    public Room getRoomWithID(int ID) {
        int index = ID - 1;
        if( index < 0 || index > 19 ) {
            throw new RuntimeException("Invalid room ID");
        }
        if( rooms[index] == null ) {
            rooms[index] = new Room(ID);
        }
        return rooms[index];
    }

    public int getRoomIDWithWumpus() {
        for(int i=0; i<20; i++) {
            if( rooms[i].hasWumpus ) {
                return rooms[i].id;
            }
        }
        return 0;
    }
}
