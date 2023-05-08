package edu.eci.arsw.threads;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameThreadTest  {

    Lobby lobby;
    final AtomicBoolean playersMoved = new AtomicBoolean(false);

    GameThread gt;

    @BeforeEach
    public void instance(){
        playersMoved.set(false);
        Player admin = new Player("admin");
        lobby = new Lobby(1, 5, 8, 10, admin);
        gt = new GameThread(lobby,playersMoved);
    }

    @Test
    public void shouldEnd() throws InterruptedException {
        //Validar 2 players
        Player player = new Player("player");
        lobby.addPlayer(player);
        gt.start();
        while(!playersMoved.get()){
            synchronized (playersMoved){
                playersMoved.wait();
            }
        }
        long start1 = System.currentTimeMillis();
        gt.join();
        long end1 = System.currentTimeMillis();
        System.out.println(end1-start1);
        assertTrue(end1-start1 < 500);
    }

}