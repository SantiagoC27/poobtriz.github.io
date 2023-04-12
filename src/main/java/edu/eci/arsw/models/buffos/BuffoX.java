package edu.eci.arsw.models.buffos;

import java.util.Timer;
import java.util.TimerTask;

import edu.eci.arsw.models.Tablero;

public class BuffoX extends Buffo{
	public BuffoX(int[] c) {
		super(c);
		super.color = "purple";
	}
	@Override
	public void activate(final Tablero t) {
		//Durante 3 segundos la ficha bajar� al doble de velocidad normal.
		final Timer timer = new Timer();
		t.setVelocidad((int) t.getVelocidad()/2);
		
		TimerTask genBuffo = new TimerTask() {
			public void run() {
				t.setVelocidad(t.getVelocidad()*2);
				this.cancel();
				timer.cancel();
			}
		};
		timer.schedule(genBuffo,3000);
		
	}
}
