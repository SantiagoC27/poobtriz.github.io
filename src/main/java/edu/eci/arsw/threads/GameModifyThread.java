package edu.eci.arsw.threads;

import edu.eci.arsw.notifiers.PlayerNotifier;

public class GameModifyThread extends Thread{

    private final PlayerNotifier player;
    private final String movement;


    public GameModifyThread(PlayerNotifier player, String movement){
        this.player = player;
        this.movement = movement;
    }

    @Override
    public void run(){
        synchronized (player.getMoved()){
            try {
                if (!player.getMoved().get()) player.getMoved().wait();

                player.moveBlock(movement);
                System.out.println("Movimiento ejecutado de " + player.getPlayer().getNick());

            } catch (Exception ex) { ex.printStackTrace();}
        }

    }
}
