package edu.eci.arsw.models;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.models.buffos.factories.BuffoFactory;
import edu.eci.arsw.models.rebordes.RebordeClassic;
import edu.eci.arsw.shared.TetrisException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableroTest {
	Tablero t;
	int filas = 20;
	int cols = 10;
	List<Tablero> tableros = new ArrayList<>();
	CommonBuffo b = new CommonBuffo();

	@BeforeEach
	public void genTablero(){
		t = new Tablero(true, 1000, "yellow", filas, cols, new ArrayList<>(), b, tableros);
		tableros.add(t);
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
		String[][] expected = new String[][]{
				new String[]{t.getBg(), t.getBg(), t.getBg(), t.getBg(),t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg()},
				new String[]{t.getBg(), t.getBg(), t.getBg(), t.getBg(),t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg()},
				new String[]{t.getBg(), t.getBg(), t.getBg(), t.getBg(),t.getBg(), t.getBg(), t.getBg(), t.getBg(), t.getBg(), "red"},
				new String[]{t.getBg(), t.getBg(), t.getBg(), t.getBg(),t.getBg(), "red", t.getBg(), t.getBg(), t.getBg(), t.getBg()},
				new String[]{"red", "red", "red", "red", "red", "red", "red", "red", "red", t.getBg()},
				new String[]{"red", "red", "red", "red", "red", t.getBg(), "red", "red", "red", "red"}
				};
		for(int i = 0; i < cols; i++) {
			t.background[filas-1][i] = "red";
			t.background[filas-2][i] = "red";
			t.background[filas-3][i] = "red";
			t.background[filas-5][i] = "red";
		}
		//obstacles
		t.background[filas-4][cols/2] = "red";
		t.background[filas-6][cols-1] = "red";
		// Line not completed
		t.background[filas-3][cols-1] = t.getBg();
		t.background[filas-1][cols/2] = t.getBg();
		t.calculatePuntuacion();
		assertEquals(20,t.getPuntuacion());

		//System.out.println(Arrays.deepToString(t.background).replaceAll("],", "],\n"));
		for (int i = 1; i < expected.length; i++) {
			assertArrayEquals(expected[expected.length - i], t.background[filas - i]);
		}

	}


	@Test
	public void testRotarBlockL() {
		List<BloqueTetris>bloques = new ArrayList<>();
		t = new Tablero(true, 1000, "yellow", 5, 8, bloques, b, tableros);
		bloques.add(new BloqueTetris(BloqueTetris.formas[1], null, BloqueTetris.colores[1], 0));
		t.spawnBlock();
		t.getBlock().setPos(6,0);

		assertFalse(t.rotarBlock());

		t.getBlock().setPos(5,0);
		for (int i = 0; i < 4; i++) {
			assertTrue(t.rotarBlock());
		}


	}

	@Test
	public void colisionTest(){
		//TODO realizar test
	}

	@Test
	public void instanceAnotherBLock() throws TetrisException{
		moveBlockDown();
		assert(t.getBlock() == null);
		t.moveBlock("DOWN");
		assert(t.getBlock() != null);
		for (int i = 0; i < t.background.length / 2; i++) {
			assertTrue(t.moveBlock("DOWN"));
		}
		assertTrue(t.moveBlock("DOWN"));
	}

	@Test
	public void shouldEndGame() throws TetrisException {
		List<BloqueTetris>bloques = new ArrayList<>();
		t = new Tablero(true, 1000, "yellow", 5, 8, bloques, b, tableros);
		t.background[0] = new String[]{"red", "red", "red", "red", "red", "red", "red", t.getBg()};
		bloques.add(new BloqueTetris(BloqueTetris.formas[0], new RebordeClassic(),"blue", 0) );
		Assertions.assertFalse(t.moveBlock("DOWN"));

		Assertions.assertTrue(t.isFinGame());

	}

	@Test
	public void shouldActivateBuffo() throws TetrisException {
		int[] position = new int[]{4,2};
		Buffo b = BuffoFactory.getRandomBuffo(position);
		t.setBuffo(b);
		t.getBlock().setPos(position[0], 0);
		for (int i = 0; i < position[1]; i++) {
			t.moveBlock("DOWN");
			assertNotNull(b);
		}
		t.moveBlock("DOWN");
		assertNull(t.getBuffo());
	}

	@Test
	public void shouldntMove() throws TetrisException {
		List<int[]> oldPos = new ArrayList<>();
		for (Tablero t :  tableros) {
			oldPos.add(t.getPositionBlock());
		}

		for (int i = 0; i < tableros.size(); i++) {
			tableros.get(i).setMovilidadBlock(false);
			tableros.get(i).moveBlock("DOWN");
			assertEquals(tableros.get(i).getPositionBlock()[1], oldPos.get(i)[1]);
		}
	}
}
