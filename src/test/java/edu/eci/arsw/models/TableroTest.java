package edu.eci.arsw.models;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.buffos.factories.BuffoFactory;
import edu.eci.arsw.shared.TetrisException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.*;

public class TableroTest {
	Tablero t;
	int filas = 20;
	int cols = 10;

	ConcurrentLinkedQueue<Buffo> buffos = new ConcurrentLinkedQueue<>();

	@Before
	public void genTablero(){
		t = new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), buffos, new ArrayList<>());
		t.spawnBlock();
	}
	
	@Test
	public void testSpawnBlock() {
		assertNotNull(t.getBlock());
	}

	@Test
	public void moveBlockDown() throws TetrisException {
		for (int i = 0; i < filas; i++) {
			assertTrue(t.moveBlock("DOWN"));
		}
		assertFalse(t.moveBlock("DOWN"));
	}

	@Test
	public void moveBlockRight() throws TetrisException{
		int[] initPos = t.getPositionBlock();
		for (int i = 1; i <= cols/2 - t.getBlock().getWidth(); i++) {
			t.moveBlock("RIGHT");
			assertEquals(initPos[0] + i, t.getPositionBlock()[0]);
		}
		assertFalse(t.moveBlock("RIGHT"));
	}

	@Test
	public void testMoveBlockLeft() throws TetrisException{
		int[] initPos = t.getPositionBlock();
		for (int i = 1; i < cols/2 + 1; i++) {
			t.moveBlock("LEFT");
			assertEquals(initPos[0] - i, t.getPositionBlock()[0]);
		}
		assertFalse(t.moveBlock("LEFT"));
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
		assertEquals(10, t.getPuntuacionBloques());
	}

	public void colisionTest(){
		//TODO realizar test
	}

	@Test
	public void instanceAnotherBLock() throws TetrisException{
		moveBlockDown();
		assert(t.getBlock() == null);
		t.moveBlock("DOWN");
		assert(t.getBlock() != null);
		for (int i = 0; i < t.getFilas() / 2; i++) {
			assertTrue(t.moveBlock("DOWN"));
		}
		assertTrue(t.moveBlock("DOWN"));
	}

	@Test
	public void shouldActivateBuffo() throws TetrisException {
		int[] position = new int[]{5,2};
		Buffo b = BuffoFactory.getRandomBuffo(position, new ArrayList<>());
		buffos.add(b);
		for (int i = 0; i < position[1]; i++) {
			t.moveBlock("DOWN");
			assertEquals(1, buffos.size());
		}
		t.moveBlock("DOWN");
		assertEquals(0, buffos.size());
	}
}
