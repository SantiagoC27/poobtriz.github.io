package edu.eci.arsw.threads;


import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.models.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PlayerThreadTest {

    final AtomicBoolean endGame = new AtomicBoolean(false);
    final int velocity = 5;

    PlayerThread pt;

    @BeforeEach
    public void instance(){
        endGame.set(false);
        pt = new PlayerThread(new Player("admin",
                new Tablero(true, velocity, "yellow", 5, 8, new ArrayList<>(), new CommonBuffo(),
                        new ArrayList<>())), endGame);
    }

    @Test
    void shouldEnd() throws InterruptedException {
        pt.start();
        while(!pt.getEndGame().get()){
            synchronized (endGame){
                endGame.wait();
            }
        }
        long start1 = System.currentTimeMillis();
        pt.join();
        long end1 = System.currentTimeMillis();
        assertTrue(end1-start1 < 500);
    }
}