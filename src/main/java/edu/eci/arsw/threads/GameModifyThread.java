package edu.eci.arsw.threads;

public class GameModifyThread extends Thread{

    private final PlayerThread player;
    private final String movement;


    public GameModifyThread(PlayerThread player, String movement){
        this.player = player;
        this.movement = movement;
    }

    @Override
    public void run(){
        try {
            synchronized (player.getEndGame()){
                if (!player.getEndGame().get()) player.moveBlock(movement);
            }
        } catch (Exception ex) { ex.printStackTrace();}

    }
}
