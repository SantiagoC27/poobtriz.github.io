package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Getter
public class BuffoLessScore extends Buffo{

    @Setter
    private int delay = 10000;
    public BuffoLessScore(int[] c){
        super(c, "left,red,orange,yellow, green, blue,indigo,violet");
    }

    @Override
    public void activate(List<Tablero> tableros, int idTablero) {
    List<Tablero> othersTableros =tableros.stream().filter(s -> s.getId() != idTablero).collect(Collectors.toList());
    int puntuacion = tableros.stream().filter(s -> s.getId() == idTablero).collect(Collectors.toList()).get(0).getSumPuntuacion().get();
    for (Tablero t: othersTableros) {
        t.getSumPuntuacion().set(puntuacion/2);
        System.out.println("tablero " +t.getId() + "seteao");
    }

    final Timer timer = new Timer();
    TimerTask resetAddPuntuacion = new TimerTask() {
        public void run() {
            for (Tablero t: othersTableros) {
                t.getSumPuntuacion().set(puntuacion);
            }
            System.out.println("tableros normalizados");
            this.cancel();
            timer.cancel();
        }
    };
    timer.schedule(resetAddPuntuacion,delay,1);


    }
}
