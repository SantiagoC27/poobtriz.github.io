package edu.eci.arsw.threads;

import com.google.gson.Gson;
import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameThread extends Thread{


    private final Lobby lobby;
    private final AtomicBoolean playersMoved;
    private List<BloqueTetris> bloques;
    Map<String, Session> session = new ConcurrentHashMap<>();

    public int getCodigo(){
        return lobby.getCodigo();
    }

    public GameThread(Lobby lobby, Map<String, Session> sessions, AtomicBoolean playersMoved){
        this.lobby = lobby;
        this.session = sessions;
        this.playersMoved = playersMoved;
    }

    @Override
    public void run(){
        instanceGame();
        int velocidad = 1000; // TODO dinamizar
        while(!lobby.endGame()){
            for (Player player: lobby.getPlayers()) {
                if(!player.moveBlockDown()){
                    player.calculatePuntuacion();
                }
            }
            playersMoved.set(true);

            try {
                Thread.sleep(velocidad);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Inicializa los tableros de los jugadores con una misma lista de bloques
     */
    private void instanceGame(){
        bloques = Collections.synchronizedList(new ArrayList<>());
        Tablero t;
        for (Player player: lobby.getPlayers()) {
            // TODO dinamizar parametros
            t = new Tablero(true, 1000, "yellow", 20, 10, bloques);
            player.setTablero(t);
        }
        lobby.setEstado(Estado.RUNNING);


    }


    public static void main(String[] args) throws Exception{
        Lobby l = new Lobby(1);
        l.addPlayer(
                new Jugador("x", new Tablero(true, 1000, "red", 20, 10, Collections.synchronizedList(new ArrayList<>()))));
        Thread gt = new Thread(new GameThread(l, null, null));
        gt.start();
    }

    public void moveBlock(String username, String movement) {
        Player player = lobby.getPlayers().stream().filter(p -> Objects.equals(p.getNick(), username)).collect(Collectors.toList()).get(0);
            try{
                switch (movement.toUpperCase()){
                    case "DOWN":
                        player.moveBlockDown();
                        break;
                    case "UP":
                        player.turnBlock();
                        break;
                    case "LEFT":
                        player.moveBlockLeft();
                        break;
                    case "RIGHT":
                        player.moveBlockRight();
                        break;
                }
            }catch (TetrisException e){
                e.printStackTrace();
            }
    }
}
