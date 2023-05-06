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
        for (BloqueTetris bloque :  bloques) {
            assertEquals(4, bloque.getCoordenadas().length);
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