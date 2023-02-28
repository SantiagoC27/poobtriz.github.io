package edu.eci.arsw.models.buffos;

import java.awt.Color;

import edu.eci.arsw.models.Tablero;

public class BuffoSD extends Buffo{
	
	public BuffoSD(int[] c) {
		super(c);
		super.c = Color.magenta;
	}

	
	public void activate(Tablero t) {
		//Detiene el bloque y seguir� bajando cuando el jugador presione la tecla de bajar.
		t.setMovilidadBlock(false);
		
	}

}
