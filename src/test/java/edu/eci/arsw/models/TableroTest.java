package edu.eci.arsw.models;

import edu.eci.arsw.shared.TetrisException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TableroTest {
	Tablero t;
	int filas = 20;
	int cols = 10;

	@Before
	public void genTablero(){
		t = new Tablero(true, 1000, "yellow", filas, cols, null);
		t.spawnBlock();
	}
	
	@Test
	public void testSpawnBlock() {
		assertNotNull(t.getBlock());
	}

	@Test
	public void moveBlockDown() throws TetrisException {
		for (int i = 0; i < filas; i++) {
			assertTrue(t.moveBlockDown());
		}
		assertFalse(t.moveBlockDown());
	}

	@Test
	public void moveBlockRight() {
		int[] initPos = t.getPositionBlock();
		for (int i = 1; i <= cols/2 - t.getBlock().getWidth(); i++) {
			t.moveBlockRight();
			assertEquals(initPos[0] + i, t.getPositionBlock()[0]);
		}
		assertFalse(t.moveBlockRight());
	}

	@Test
	public void testMoveBlockLeft() {
		int[] initPos = t.getPositionBlock();
		for (int i = 1; i < cols/2 + 1; i++) {
			t.moveBlockLeft();
			assertEquals(initPos[0] - i, t.getPositionBlock()[0]);
		}
		assertFalse(t.moveBlockLeft());
	}

	@Test
	public void testClearLines() {
		//Fill two lines completed
		for(int i = 0; i < cols; i++) {
			t.background[filas-1][i] = "red";
			t.background[filas-2][i] = "red";
			t.background[filas-3][i] = "red";
		}
		// Line not completed
		t.background[filas-3][cols-1] = t.getBg();


		t.clearLines();
		for(int j = 0; j < cols; j++) {
			assertEquals(t.background[filas-2][j], t.getBg());
			assertEquals(t.background[filas-3][j], t.getBg());
		}

		//Line doesn't cleared
		for (int k = 0; k < cols - 1; k++) {
			assertEquals(t.background[filas-1][k], "red");
		}
	}


	@Test
	public void testRotarBlock() {
		// TODO Terminar
	}


	@Test
	public void DeberiaSumarPuntuacion(){
		for(int i = 0; i <= 9; i++) {
			t.background[19][i] = "red";
		}
		t.addPuntuacion(t.clearLines());
		assertEquals(1, t.getPuntuacionBloques());
	}
}
