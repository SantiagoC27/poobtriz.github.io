package edu.eci.arsw.threads;

import com.google.gson.Gson;
import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameThread implements Runnable{

    private static final Gson gson = new Gson();
    private final Lobby lobby;
    private ConcurrentLinkedQueue<BloqueTetris> bloques;
    Map<String, Session> session = new ConcurrentHashMap<>();


    public GameThread(Lobby lobby, Map<String, Session> sessions) throws Exception{
        this.lobby = lobby;
        for (Player p: this.lobby.getPlayers()) {
            Session aux = sessions.get(p.getNick());
            if (aux == null) throw new TetrisException(TetrisException.INVALID_SESSION);
            this.session.put(p.getNick(), aux);
        }
    }

    @Override
    public void run(){
        instanceGame();
        int velocidad = 3000; // TODO dinamizar
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
        bloques = new ConcurrentLinkedQueue<>();
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
}
