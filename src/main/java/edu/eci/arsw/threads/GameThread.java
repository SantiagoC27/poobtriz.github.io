package edu.eci.arsw.threads;

import com.google.gson.Gson;
import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameThread extends Thread{

    private static final Gson gson = new Gson();
    private final Lobby lobby;
    private List<BloqueTetris> bloques;
    Map<String, Session> session = new ConcurrentHashMap<>();

    public int getCodigo(){
        return lobby.getCodigo();
    }

    public GameThread(Lobby lobby, Map<String, Session> sessions) throws Exception{
        this.lobby = lobby;
        for (Player p: this.lobby.getPlayers()) {
            Session aux = sessions.get(p.getNick());
            if (aux == null) throw new TetrisException(TetrisException.INVALID_SESSION);
            this.session.put(p.getNick(), aux);
        }
    }

    public GameThread(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public void run(){
        instanceGame();
        int velocidad = 1000; // TODO dinamizar
        while(!lobby.endGame()){
            for (Player player: lobby.getPlayers()) {
                synchronized (player){
                    if(!player.moveBlockDown()){
                        player.calculatePuntuacion();
                    }
                }
            }
            broadcast();
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

    }

    /**
     * Emite a los usuarios de la sesión el lobby en formato string
     */
    private void broadcast() {
        // TODO find out https://www.baeldung.com/gson-exclude-fields-serialization
        session.values().forEach(s -> {
            s.getAsyncRemote().sendObject(gson.toJson(lobby), result -> {
                if (result.getException() != null) {
                    System.out.println("No se pudo trasmitir la información a los integrantes del lobby. " + result.getException());
                }
            });
        });
    }


    public static void main(String[] args) throws Exception{
        Lobby l = new Lobby(1);
        l.addPlayer(
                new Jugador("x", new Tablero(true, 1000, "red", 20, 10, Collections.synchronizedList(new ArrayList<>()))));
        Thread gt = new Thread(new GameThread(l));
        gt.start();
    }

    public void moveBlock(String username, String movement) {
        Player player = lobby.getPlayers().stream().filter(p -> Objects.equals(p.getNick(), username)).collect(Collectors.toList()).get(0);
        synchronized (player){
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
        broadcast();

    }
}
