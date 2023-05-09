package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.shared.TetrisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuffoStopDieceTest {

    int filas = 5;
    int cols = 10;
    List<Tablero> tableros = new ArrayList<>();

    final CommonBuffo b = new CommonBuffo();

    @BeforeEach
    public void genTablero(){
        List<BloqueTetris> bloques = new ArrayList<>();
        bloques.add(new BloqueTetris(BloqueTetris.formas[1], null, BloqueTetris.colores[1], 0));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, bloques, b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, bloques, b, tableros));
        tableros.add(new Tablero(true, 1000, "yellow", filas, cols, bloques, b, tableros));
        for (Tablero t : tableros) {
            t.spawnBlock();
        }
        for (Tablero t : tableros) {
            t.spawnBlock();
        }
        b.set(new BuffoStopDiece(new int[]{cols/2, 0}));

    }

    @Test
    public void shouldActivate() throws TetrisException {
        // Mover un tablero aleatoriamente y se valida que solo ese se haya movido
        List<int[]> oldPos = new ArrayList<>();
        for (Tablero t :  tableros) {
            oldPos.add(t.getPositionBlock());
        }

        int iTablero =(int) (Math.random() * tableros.size());
        tableros.get(iTablero).moveBlock("DOWN");
        oldPos.set(iTablero, tableros.get(iTablero).getPositionBlock());
        assertNull(b.get());
        for (Tablero t :  tableros) t.moveBlock("DOWN");

        for (int i = 0; i < tableros.size(); i++) {
            if (i == iTablero) assertTrue(tableros.get(i).getPositionBlock()[1] != oldPos.get(i)[1]);
            else assertArrayEquals(tableros.get(i).getPositionBlock(), oldPos.get(i));
        }
    }
}