package edu.eci.arsw.models.buffos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BuffoLessScore extends Buffo{

    @Setter
    private int delay = 10000;

    private final Timer timer;
    public BuffoLessScore(int[] c){
        super(c, "lessScore");
        timer = new Timer();
    }

    @Override
    public void activate(List<Tablero> tableros, int idTablero) {
    List<Tablero> othersTableros =tableros.stream().filter(s -> s.getId() != idTablero).collect(Collectors.toList());
    int puntuacion = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0).getSumPuntuacion().get();
    for (Tablero t: othersTableros) {
        t.getSumPuntuacion().set(puntuacion/2);
    }

    TimerTask resetAddPuntuacion = new TimerTask() {
        public void run() {
            for (Tablero t: othersTableros) {
                t.getSumPuntuacion().set(puntuacion);
            }

            synchronized (timer){
                timer.notify();
            }
        }
    };
    timer.schedule(resetAddPuntuacion,delay);


    }
}
