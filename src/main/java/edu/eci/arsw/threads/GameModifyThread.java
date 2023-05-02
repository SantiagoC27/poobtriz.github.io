package edu.eci.arsw.threads;

import edu.eci.arsw.notifiers.PlayerNotifier;

public class GameModifyThread extends Thread{

    private final PlayerNotifier player;
    private final String user;
    private final String movement;


    public GameModifyThread(GameThread gt, String user, String movement){
        this.player = gt.getPlayer(user);
        this.user = user;
        this.movement = movement;
    }

    @Override
    public void run(){
        synchronized (player.getMoved()){
            try {
            if (!player.getMoved().get()) player.getMoved().wait();

            player.moveBlock(movement);
            System.out.println("Movimiento ejecutado de " + user);

            } catch (Exception ignored) {}
        }

    }
}
