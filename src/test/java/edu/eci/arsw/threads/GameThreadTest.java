package edu.eci.arsw.threads;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;


public class GameThreadTest  {

    Lobby lobby;
    AtomicBoolean playersMoved;

    GameThread gt;

    @Before
    public void instance(){
        playersMoved = new AtomicBoolean(false);
        Player admin = new Player("admin");
        lobby = new Lobby(1, 7, 10, 1000, admin);
        gt = new GameThread(lobby,playersMoved);
    }

    @Test
    public void shouldPlay2Players() throws InterruptedException {
        Player player = new Player("player");
        lobby.addPlayer(player);
        gt.start();
        gt.join();
    }

}