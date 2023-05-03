package edu.eci.arsw.models.buffos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import edu.eci.arsw.models.Tablero;

public class BuffoS extends Buffo{

	public BuffoS(int[] c) {
		super(c);
		super.color = "green";
	}
	
	@Override
	public void activate(final List<Tablero> tableros, int idTablero) {
		Tablero t = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0);
		//Las fichas bajan mas lento por 3 segundos
		final Timer timer = new Timer();
		t.setVelocidad(t.getVelocidad()*2);
		
		TimerTask genBuffo = new TimerTask() {
			public void run() {
					t.setVelocidad((int) t.getVelocidad()/2);
					this.cancel();
					timer.cancel();
			}
		};
		timer.schedule(genBuffo,3000,3000);
		
	}
	

}
