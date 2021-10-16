package pl.felixspeagel.wumpus.game_logic;

import java.util.Random;

public class CaveBuilder {
    private int pitsCount;
    private int batsCount;
    private int arrowsCount;

    public CaveBuilder() {
        //default settings
        pitsCount = 2;
        batsCount = 2;
        arrowsCount = 5;
    }

    public void setPits(int count) {
        pitsCount = count;
    }
    public void setBats(int count) {
        batsCount = count;
    }
    public void setArrows(int count) {
        arrowsCount = count;
    }

    public Cave build() {
        Cave cave = new Cave(arrowsCount);

        //connect rooms
        for(int i=1; i<20; i++) {
            cave.getRoomWithID(i).addConnection(cave.getRoomWithID(i+1));
        }
        cave.getRoomWithID(5).addConnection(cave.getRoomWithID(1));
        cave.getRoomWithID(15).addConnection(cave.getRoomWithID(6));
        cave.getRoomWithID(20).addConnection(cave.getRoomWithID(16));
        for(int i=1; i<5; i++) {
            cave.getRoomWithID(i).addConnection(cave.getRoomWithID(7+i+(i-1)));
        }
        for(int i=7; i<14; i+=2) {
            cave.getRoomWithID(i).addConnection(cave.getRoomWithID(i+10-(i-7)/2));
        }

        //place hazards
        Random random = new Random();
        int placed, chosen;
        Room chosenRoom;

        placed = 0;
        while(placed < batsCount) {
            chosen = random.nextInt(1,21);
            chosenRoom = cave.getRoomWithID(chosen);
            if( chosenRoom.hasBats) continue;
            chosenRoom.hasBats = true;
            placed++;
        }

        placed = 0;
        while(placed < pitsCount) {
            chosen = random.nextInt(1,21);
            chosenRoom = cave.getRoomWithID(chosen);
            if( chosenRoom.hasBats || chosenRoom.hasPit ) continue;
            chosenRoom.hasPit = true;
            placed++;
        }

        placed = 0;
        while(placed < 1) {
            chosen = random.nextInt(1,21);
            chosenRoom = cave.getRoomWithID(chosen);
            if( chosenRoom.hasBats || chosenRoom.hasPit ) continue;
            chosenRoom.hasWumpus = true;
            placed++;
        }

        //place player
        placed = 0;
        while(placed < 1) {
            chosen = random.nextInt(1,21);
            chosenRoom = cave.getRoomWithID(chosen);
            if( chosenRoom.hasBats || chosenRoom.hasPit || chosenRoom.hasWumpus ) continue;
            cave.playerPos = chosen;
            placed++;
        }

        return cave;
    }
}
