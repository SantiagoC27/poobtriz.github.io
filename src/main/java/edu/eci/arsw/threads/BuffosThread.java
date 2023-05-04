package edu.eci.arsw.threads;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.buffos.factories.BuffoFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BuffosThread implements Runnable{

    private final TimerTask genBuffo;
    private final Timer timer;

    private final int time;

    BuffosThread(ConcurrentLinkedQueue<Buffo> buffos, int time, int filas, int cols, List<String> colors, AtomicBoolean end){
        this.time = time;
        timer = new Timer();
        genBuffo = new TimerTask() {
            public void run() {
                if (end.get()){
                    this.cancel();
                    timer.cancel();
                }

                int[] coord = new int[]{(int) (Math.random()*filas), (int) (Math.random()*cols)};
                buffos.add(BuffoFactory.getRandomBuffo(coord, colors));
            }
        };
    }

    @Override
    public void run(){
        timer.schedule(genBuffo, time, time);
    }
}
