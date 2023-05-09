package edu.eci.arsw.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BloqueTetrisTest {

    List<BloqueTetris> bloques;

    @BeforeEach
    public void setup() {
        bloques = new ArrayList<>();
        for (int[][] form :BloqueTetris.formas) {
            for (int i = 0; i < 4; i++) {
                bloques.add(new BloqueTetris(form, null, null, 0));
            }
        }
    }


    @Test
    public void getCoordenadas() {
        int[][] oldCoords;
        for (BloqueTetris bloque :  bloques) {
            oldCoords =bloque.getCoordenadas();
            assertEquals(4, oldCoords.length);
        }
    }

    @Test
    public void shouldMoveLeft() {
        int[][] oldCoords, newCoords;
        for (BloqueTetris bloque :  bloques) {
            oldCoords =bloque.getCoordenadas();
            bloque.moveLeft();
            newCoords =bloque.getCoordenadas();
            for (int i = 0; i < oldCoords.length; i++) {
                assertEquals(oldCoords[i][0], newCoords[i][0]+1);
            }
        }
    }

    @Test
    public void shouldMoveRight() {
        int[][] oldCoords, newCoords;
        for (BloqueTetris bloque :  bloques) {
            oldCoords =bloque.getCoordenadas();
            bloque.moveRight();
            newCoords =bloque.getCoordenadas();
            for (int i = 0; i < oldCoords.length; i++) {
                assertEquals(oldCoords[i][0], newCoords[i][0]-1);
            }
        }
    }

    @Test
    public void shouldMoveDown() {
        int[][] oldCoords, newCoords;
        for (BloqueTetris bloque :  bloques) {
            oldCoords =bloque.getCoordenadas();
            bloque.moveDown();
            newCoords =bloque.getCoordenadas();
            for (int i = 0; i < oldCoords.length; i++) {
                assertEquals(oldCoords[i][1], newCoords[i][1]-1);
            }
        }
    }

    @Test
    public void shouldntMoveDown() {
        int[][] oldCoords, newCoords;
        for (BloqueTetris bloque :  bloques) {
            oldCoords =bloque.getCoordenadas();
            bloque.setMobibleDown(false);
            bloque.moveDown();
            newCoords =bloque.getCoordenadas();
            assertArrayEquals(oldCoords, newCoords);
        }
    }

    @Test
    public void getCoordenadasCercanas() {
        for (int i = 4; i < bloques.size(); i++) {
            assertEquals(14, bloques.get(i).getCoordenadasCercanas().size());
        }
        for (int i = 0; i < 4; i++) {
            assertEquals(12, bloques.get(i).getCoordenadasCercanas().size());
        }
    }
}