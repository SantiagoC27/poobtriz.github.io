package edu.eci.arsw.threads;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.concurrent.atomic.AtomicBoolean;


public class GameThreadTest  {

    Lobby lobby;
    AtomicBoolean playersMoved;

    GameThread gt;

    @BeforeEach
    public void instance(){
        playersMoved = new AtomicBoolean(false);
        Player admin = new Player("admin");
        lobby = new Lobby(1, 5, 8, 10, admin);
        gt = new GameThread(lobby,playersMoved);
    }

    @Test
    public void shouldEnd() throws InterruptedException {
        Player player = new Player("player");
        lobby.addPlayer(player);
        gt.start();
        gt.join();
    }

}