package edu.eci.arsw.threads;

import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class PlayerThread extends Thread{

    private final Player player;

    private final AtomicBoolean endGame;

    public PlayerThread(Player player, AtomicBoolean endGame){
        this.player = player;
        this.endGame = endGame;
    }

    public synchronized void moveBlock(String movement) throws TetrisException {
        synchronized (endGame){
            this.player.moveBlock(movement);
            this.endGame.notify();
        }
    }

    public boolean isEmptyCoord(int[] coord) {
        return player.isEmptyCoord(coord);
    }

    @Override
    public void run() {
        while (!player.hasFinished()){
            try {
                synchronized (endGame){
                    player.moveBlock("DOWN");
                    endGame.notify();
                }
                Thread.sleep(player.getVelocity());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        synchronized (endGame){
            this.endGame.set(true);
            this.endGame.notify();
        }
        System.out.println(player.getNick() + " acabo");

    }
}
