package edu.eci.arsw.models.rebordes;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RebordeUselessTest {

    Tablero t;
    int filas = 20;
    int cols = 10;

    List<BloqueTetris> bloques;

    CommonBuffo b = new CommonBuffo();

    @BeforeEach
    public void genTablero(){
        bloques = new ArrayList<>();
        t = new Tablero(true, 1000, "yellow", filas, cols, bloques, b, new ArrayList<>());
    }

    @Test
    public void shouldNotErase(){
        String[][] expectedColors = new String[][]{
                new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", "red"},
                new String[]{t.getBg(), t.getBg(), t.getBg(), t.getBg(),t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg()},
        };
        Reborde[][] rebordes = new Reborde[][]{
                new Reborde[]{null, null, null, null, null, null, null, null, null, new RebordeUseless()},
                new Reborde[]{null, null, null, null, null, null, null, null, null, null}
        };

        for (int i = 0; i < rebordes.length; i++) {
            genTablero();
            t.background[5] = new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", "red"};
            t.bgReborde[5] = rebordes[i];
            t.calculatePuntuacion();
            assertEquals(10 * i, t.getPuntuacion());
            assertArrayEquals(t.background[5], expectedColors[i]);
            assertArrayEquals(t.bgReborde[5], rebordes[i]);
        }
    }
}