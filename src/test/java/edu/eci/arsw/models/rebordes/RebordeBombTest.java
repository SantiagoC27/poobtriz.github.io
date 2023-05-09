package edu.eci.arsw.models.rebordes;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.shared.TetrisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RebordeBombTest {
    Tablero t;
    int filas = 20;
    int cols = 10;

    int repeticiones = 4;

    List<BloqueTetris> bloques;

    CommonBuffo b = new CommonBuffo();

    @BeforeEach
    public void genTablero(){
        bloques = new ArrayList<>();
        t = new Tablero(true, 1000, "yellow", filas, cols, bloques, b, new ArrayList<>());
    }

    @Test
    public void eraseNeighbors() throws TetrisException {
        BloqueTetris bloque = null;
        for (int i = 0; i < repeticiones; i++) {
            bloques.add(BloqueTetris.selectRandomBlock(t.getBg(), new RebordeBomb()));
            bloques.add(BloqueTetris.selectRandomBlock(t.getBg(), new RebordeBomb()));
            while (t.moveBlock("DOWN")){
                bloque = t.getBlock().Clone();
            }
            assertNotNull(bloque);
            for (int[] co : bloque.getCoordenadas()) {
                    assertEquals(t.getBg(), t.background[co[1]][co[0]]);
            }
            for (int[] coord : bloque.getCoordenadasCercanas()) {
                if (coord[0] < filas && coord[0] >= 0 && coord[1] < cols && coord[1] >= 0 )
                    assertEquals(t.getBg(), t.background[coord[1]][coord[0]]);
            }
        }
    }

    @Test
    public void eraseNeighbors2() throws TetrisException {
        t.background[filas-1] = new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", t.getBg()};
        t.background[filas-2] = new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", t.getBg()};
        t.background[filas-3] = new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", t.getBg()};
        t.background[filas-4] = new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", t.getBg()};
        this.eraseNeighbors();
        assertTrue(Arrays.stream(t.background[filas-1]).anyMatch(c -> c.equals(t.getBg())) &&
                Arrays.asList(t.background[filas - 1]).contains("red"));
        assertTrue(Arrays.asList(t.background[filas - 2]).contains("red"));
        assertTrue(Arrays.asList(t.background[filas - 3]).contains("red"));
        assertTrue(Arrays.asList(t.background[filas - 4]).contains("red"));
    }
}