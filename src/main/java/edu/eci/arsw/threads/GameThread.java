package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.models.buffos.factories.BuffoFactory;
import edu.eci.arsw.models.player.Player;
import lombok.Getter;

public class GameThread extends Thread{

    private final Lobby lobby;

    @Getter
    private final AtomicBoolean playerMoved;

    private final List<PlayerThread> players = new ArrayList<>();

    private final TimerTask genBuffo;
    private final Timer timer;

    private final CommonBuffo b = new CommonBuffo();


    public GameThread(Lobby lobby, AtomicBoolean playerMoved){
        this.lobby = lobby;
        this.playerMoved = playerMoved;
        timer = new Timer();
        genBuffo = instanceBuffosTask();
    }


    @Override
    public void run(){
        instanceGame();
        for (PlayerThread player : players) player.start();
        for (PlayerThread player : players) {
            try {
                player.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Acabo gameThread\n");
        genBuffo.cancel();
        timer.cancel();
        lobby.setEstado(Estado.FINISHED);
    }

    /**
     * Inicializa los tableros de los jugadores con una misma lista de bloques
     */
    private void instanceGame(){
        List<BloqueTetris> bloques = Collections.synchronizedList(new ArrayList<>());
        List<Tablero> tableros = new ArrayList<>();
        Player player;

        for (int i = 0; i < lobby.getPlayers().size(); i++) {
            player = lobby.getPlayers().get(i);
            tableros.add(new Tablero(true, lobby.getVelocity(), player.getColorTablero(),
                    lobby.getFilas(), lobby.getCols(), bloques, b, tableros));
            player.setTablero(tableros.get(i));
            players.add(new PlayerThread(player, playerMoved));
        }
        timer.schedule(genBuffo, 10000, 10000);
        lobby.setEstado(Estado.RUNNING);
    }

    public void moveBlock(String username, String movement) {
        PlayerThread playerNot = players.stream().filter(p -> Objects.equals(p.getPlayer().getNick(), username))
                .collect(Collectors.toList()).get(0);
        try {
            GameModifyThread gmt = new GameModifyThread(playerNot, movement);
            gmt.start();
            gmt.join();
        } catch (InterruptedException ignored) {}
    }

    private TimerTask instanceBuffosTask(){
        return new TimerTask() {
            public void run() {
                int[] c = new int[]{(int) (Math.random()*lobby.getFilas()), (int) (Math.random()*lobby.getCols())};
                b.set(BuffoFactory.getRandomBuffo(c));
            }
        };

    }


}

