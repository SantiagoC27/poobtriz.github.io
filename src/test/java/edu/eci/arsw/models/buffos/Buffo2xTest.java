package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.shared.TetrisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class Buffo2xTest{

    int filas = 5;
    int cols = 10;
    List<Tablero> tableros = new ArrayList<>();

    final CommonBuffo b = new CommonBuffo();

    Buffo2x buff;

    @BeforeEach
    public void genTablero(){
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros));
        for (Tablero t : tableros) {
            t.spawnBlock();
        }
        buff =new Buffo2x(new int[]{cols/2, 0});
        buff.setDelay(1000);
        b.set(buff);
    }

    @Test
    public void shouldActivate() throws TetrisException, InterruptedException {
        int iTablero =(int) (Math.random() * tableros.size());
        tableros.get(iTablero).moveBlock("DOWN");
        assertNull(b.get());
        for (Tablero t : tableros) {
            assertTrue(t.getVelocidad() >= tableros.get(iTablero).getVelocidad());
        }

        synchronized (buff.getTimer()){
            buff.getTimer().wait();
            for (Tablero t : tableros) assertEquals(tableros.get(iTablero).getVelocidad(), t.getVelocidad());
        }

    }
}