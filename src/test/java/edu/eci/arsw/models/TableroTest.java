package dominio;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import org.junit.jupiter.api.Test;

class TableroTest {
	Tablero t = new Tablero(true, 1000, Color.yellow);
	
	
	@Test
	void testSpawnBlock() {
		t.spawnBlock();
		assertFalse(t.getBlock() == null);
	}

	@Test
	void deberiaGenerarAleatorio(){
		t.spawnBlock();
		BloqueTetris b1 = t.getBlock();
		t.addBloquesU(1);
		t.spawnBlock();
		BloqueTetris b2 = t.getBlock();
		t.addBloquesU(1);
		t.spawnBlock();
		BloqueTetris b3 = t.getBlock();
		t.addBloquesU(1);
		assertTrue(b1.getForm() != b2.getForm() || b1.getForm() != b3.getForm() ||  b2.getForm() != b3.getForm());

	}

	@Test
	void testMoveBlockDown() throws TetrisException {
		t.spawnBlock();
		t.moveBlockDown();
		assertTrue(t.getFinishGood());
	}

	@Test
	void testMoveBlockRight() {
		t.spawnBlock();
		t.moveBlockRight();
		assertTrue(t.getFinishGood());
	}

	@Test
	void testMoveBlockLeft() {
		t.spawnBlock();
		t.moveBlockLeft();
		assertTrue(t.getFinishGood());
	}

	@Test
	void testClearLines() {
		for(int i = 0; i <= 9; i++) {
			t.background[19][i] = Color.red;				
		}
		t.clearLines();
		for(int j = 0; j <= 9; j++) {
			assertTrue(t.background[19][j] == t.getBg());
		}
	}


	@Test
	void testRotarBlock() {
		t.spawnBlock();
		t.rotarBlock();
		assertTrue(t.getFinishGood());
		
	}


	@Test
	void DeberiaSumarPuntuacion(){
		for(int i = 0; i <= 9; i++) {
			t.background[19][i] = Color.red;				
		}
		t.addPuntuacion(t.clearLines());
		assertTrue(t.getPuntuacionBloques() == 1 );
		
	}

}
