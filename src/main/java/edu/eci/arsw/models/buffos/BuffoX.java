package edu.eci.arsw.models.buffos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import edu.eci.arsw.models.Tablero;

public class BuffoX extends Buffo{
	public BuffoX(int[] c) {
		super(c, "purple");
	}
	@Override
	public void activate(List<Tablero> tableros, int idTablero) {
		Tablero t = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0);
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
