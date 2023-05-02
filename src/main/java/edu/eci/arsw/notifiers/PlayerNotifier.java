package edu.eci.arsw.notifiers;

import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class PlayerNotifier {

    private final Player player;
    private final AtomicBoolean moved;

    public PlayerNotifier(Player player, AtomicBoolean moved){
        this.player = player;
        this.moved = moved;
    }

    public synchronized void moveBlock(String movement) throws TetrisException {
        synchronized (moved){
            this.player.moveBlock(movement);
            this.moved.set(true);
            this.moved.notify();
        }



    }
}
