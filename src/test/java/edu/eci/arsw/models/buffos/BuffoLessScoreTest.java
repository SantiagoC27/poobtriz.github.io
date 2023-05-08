package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.shared.TetrisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuffoLessScoreTest{
    int filas = 5;
    int cols = 10;
    List<Tablero> tableros = new ArrayList<>();

    final CommonBuffo b = new CommonBuffo();

    BuffoLessScore buff;

    @BeforeEach
    public void genTablero(){
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        for (Tablero t : tableros) {
                t.spawnBlock();
        }
        buff =new BuffoLessScore(new int[]{cols/2, 0});
        buff.setDelay(1000);
        b.set(buff);
    }

    @Test
    public void shouldActivate() throws TetrisException, InterruptedException {
        tableros.get(0).moveBlock("DOWN");
        assertNull(b.get());
        for (int i = 2; i < tableros.size(); i++) {
            assertEquals(tableros.get(i).getSumPuntuacion().get(), tableros.get(i - 1).getSumPuntuacion().get());
        }
        assertNotEquals(tableros.get(0).getSumPuntuacion().get(), tableros.get(1).getSumPuntuacion().get());
        Thread.sleep(buff.getDelay()+10);

        for (int i = 1; i < tableros.size(); i++) {
            assertEquals(tableros.get(i).getSumPuntuacion().get(), tableros.get(i - 1).getSumPuntuacion().get());
        }





    }
}