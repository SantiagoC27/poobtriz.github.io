package edu.eci.arsw.threads;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.notifiers.PlayerNotifier;
import edu.eci.arsw.shared.TetrisException;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameThread extends Thread{

    private final Lobby lobby;
    private final AtomicBoolean playersMoved;

    private final AtomicBoolean endGame = new AtomicBoolean(false);

    private final List<PlayerNotifier> players = new ArrayList<>();

    private final Thread tbuffos;

    private final ConcurrentLinkedQueue<Buffo> buffos =new ConcurrentLinkedQueue<>();

    public int getCodigo(){
        return lobby.getCodigo();
    }

    public GameThread(Lobby lobby, AtomicBoolean playersMoved){
        this.lobby = lobby;
        this.playersMoved = playersMoved;
        this.tbuffos = new Thread(new BuffosThread(buffos, 10000, lobby.getFilas(),
                lobby.getCols(), lobby.getColorsTableros(), endGame));
    }

    @Override
    public void run(){
        instanceGame();
        tbuffos.start();
        while(!lobby.endGame()){
            for (PlayerNotifier player : players) {
                try {
                    synchronized (player.getMoved()){
                        player.getMoved().set(false);
                        player.moveBlock("DOWN");
                        player.getMoved().set(true);
                        player.getMoved().notify();
                    }
                }catch (TetrisException ex){
                    ex.printStackTrace();
                }
            }
            synchronized (playersMoved){
                playersMoved.set(true);
                playersMoved.notify();
            }

            try {
                Thread.sleep(lobby.getVelocity());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        endGame.set(true);
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
                    lobby.getFilas(), lobby.getCols(), bloques, buffos, tableros));
            player.setTablero(tableros.get(i));
            players.add(new PlayerNotifier(player, new AtomicBoolean(false)));
        }
        lobby.setEstado(Estado.RUNNING);
    }

    public void moveBlock(String username, String movement) {
        PlayerNotifier playerNot = players.stream().filter(p -> Objects.equals(p.getPlayer().getNick(), username))
                .collect(Collectors.toList()).get(0);
        try {
            GameModifyThread gmt = new GameModifyThread(playerNot, movement);
            gmt.start();
            gmt.join();
        } catch (InterruptedException ignored) {}
    }
}

