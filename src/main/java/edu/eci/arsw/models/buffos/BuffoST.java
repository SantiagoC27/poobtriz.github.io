package edu.eci.arsw.models.buffos;

import java.util.Timer;
import java.util.TimerTask;

import edu.eci.arsw.models.Tablero;

public class BuffoST extends Buffo{
	public BuffoST(int[] c) {
		super(c);
		super.color = "yellow";
	}

	public void activate(final Tablero t) {
		//Hace que el bloque no baje autom�ticamente por 3 segundos

		final Timer timer = new Timer();
		final int beforeVel = t.getVelocidad();
		t.setVelocidad(3000);
		TimerTask genBuffo = new TimerTask() {		
			public void run() {
				t.setVelocidad(beforeVel);
				this.cancel();
				timer.cancel();
				
			}
		};
		timer.schedule(genBuffo,0);
	}
	
	
}
