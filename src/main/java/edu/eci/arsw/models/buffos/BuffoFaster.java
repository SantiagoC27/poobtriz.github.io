package edu.eci.arsw.models.buffos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;
import lombok.Setter;


@Getter
public class BuffoFaster extends Buffo{

	@Setter
	private int delay =10000;

	private final Timer timer;
	public BuffoFaster(int[] c) {
		super(c, "x2");
		timer = new Timer();
	}
	@Override
	public void activate(List<Tablero> tableros, int idTablero) {
		Tablero t = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0);

		t.setVelocidad(t.getVelocidad()/2);
		
		TimerTask genBuffo = new TimerTask() {
			public void run() {
				t.setVelocidad(t.getVelocidad()*2);
				synchronized (timer){
					timer.notify();
				}
			}
		};
		timer.schedule(genBuffo, delay);
		
	}
}
