package edu.eci.arsw.models.buffos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BuffoSlow extends Buffo{

	@Setter
	private int delay = 10000;

	private final Timer timer;

	public BuffoSlow(int[] c) {
		super(c, "slow");
		timer= new Timer();
	}
	
	@Override
	public void activate(final List<Tablero> tableros, int idTablero) {
		for (Tablero t : tableros) {
			if (t.getId() != idTablero) t.setVelocidad(t.getVelocidad()*2);
		}

		TimerTask genBuffo = new TimerTask() {
			public void run() {
				for (Tablero t : tableros) {
					if (t.getId() != idTablero) t.setVelocidad(t.getVelocidad()/2);
				}

				synchronized (timer){
					timer.notify();
				}
			}
		};
		timer.schedule(genBuffo,delay);
		
	}
	

}
