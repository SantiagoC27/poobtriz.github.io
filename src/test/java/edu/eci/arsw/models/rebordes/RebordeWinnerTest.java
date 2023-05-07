package edu.eci.arsw.models.rebordes;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.shared.TetrisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RebordeWinnerTest{

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
    public void ShouldCompleteLine() throws TetrisException {
        for (int i = 0; i < BloqueTetris.formas.length; i++) {
            bloques.add(new BloqueTetris(BloqueTetris.formas[i], new RebordeWinner(), "blue", 0));
            t.background[filas-1] = new String[]{"red", "red", "red", "red", "red", t.getBg(),"red", "red", "red", "red"};
            //Preparar un terreno donde quepa una T
            while (t.moveBlock("DOWN")) {}

            assertEquals(t.background[filas - 1][0], t.getBg());
        }


    }
}