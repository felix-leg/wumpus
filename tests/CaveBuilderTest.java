import org.junit.jupiter.api.Test;
import pl.felixspeagel.wumpus.game_logic.Cave;
import pl.felixspeagel.wumpus.game_logic.CaveBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class CaveBuilderTest {

    @Test
    public void correctRoomConnections() {
        //Given
        CaveBuilder builder = new CaveBuilder();
        //When
        Cave cave = builder.build();
        //Then
        for(int i=1; i<21; i++) {
            if( i<20 ) assertTrue(cave.getRoomWithID(i).isConnectedTo(i+1), String.format("Room %d must be connected to %d", i, i+1));
            if( i>1 ) assertTrue(cave.getRoomWithID(i).isConnectedTo(i-1), String.format("Room %d must be connected to %d", i, i-1));
        }
        int[] from = {1,6,16,1,2,3,4,7,9,11,13};
        int[] to = {5,15,20,8,10,12,14,17,18,19,20};
        for(int i=0; i<from.length;i++) {
            assertTrue(cave.getRoomWithID(from[i]).isConnectedTo(to[i]), String.format("Room %d must be connected to %d", from[i], to[i]));
            assertTrue(cave.getRoomWithID(to[i]).isConnectedTo(from[i]), String.format("Room %d must be connected to %d", to[i], from[i]));
        }
    }

    @Test
    public void correctHazardsCount() {
        //Given
        CaveBuilder builder = new CaveBuilder();
        final int BATS_COUNT = 2;
        final int PITS_COUNT = 2;
        //When
        builder.setBats(BATS_COUNT);
        builder.setPits(PITS_COUNT);
        Cave cave = builder.build();
        //Then
        int count;

        count = 0;
        for(int i=1; i<21; i++) {
            if( cave.getRoomWithID(i).hasBats ) count++;
        }
        assertEquals(BATS_COUNT, count, "Bats count must be equal to BATS_COUNT");

        count = 0;
        for(int i=1; i<21; i++) {
            if( cave.getRoomWithID(i).hasPit ) count++;
        }
        assertEquals(PITS_COUNT, count, "Pits count must be equal to PITS_COUNT");

        count = 0;
        for(int i=1; i<21; i++) {
            if( cave.getRoomWithID(i).hasWumpus ) count++;
        }
        assertEquals(1, count, "There must be only one Wumpus");
    }
}
