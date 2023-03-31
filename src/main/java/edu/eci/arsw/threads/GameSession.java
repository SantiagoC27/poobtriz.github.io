package edu.eci.arsw.threads;

import com.google.gson.Gson;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameSession extends Thread{
    private static final Gson gson = new Gson();

    private Lobby lobby;
    private  Map<String, Session> sessions;

    private GameThread gt;

    private final AtomicBoolean playersMoved;

    public GameSession(Lobby lobby, Map<String, Session> allSessions){
        this.lobby = lobby;
        this.sessions = allSessions;
        this.playersMoved = new AtomicBoolean(false);
        gt =new GameThread(this.lobby, this.sessions, this.playersMoved);
    }

    /**
     * Filtra dentro de allSessions las sesiones correspondientes a los miembros del lobby
     * @param lobby lobby
     * @param allSessions Todas las sesiones del websocket
     * @return
     */
    public static Map<String, Session> getSessions(Lobby lobby, Map<String, Session> allSessions) throws TetrisException {
        Map<String, Session> session = new ConcurrentHashMap<>();

        for (Player p: lobby.getPlayers()) {
            Session aux = allSessions.get(p.getNick());
            if (aux == null) throw new TetrisException(TetrisException.INVALID_SESSION);
            session.put(p.getNick(), aux);
        }
        return session;
    }

    @Override
    public void run(){
        this.gt.start();
        while (gt.isAlive()){
            if (playersMoved.get()){
                playersMoved.set(false);
                broadcast();
            }
        }
    }

    public void moveBlock(String user, String movement){
        this.gt.moveBlock(user, movement);
        System.out.println("Movimiento aceptado");
        broadcast();
    }

    public int getCodigoLobby(){
        return this.lobby.getCodigo();
    }

    /**
     * Emite a los usuarios de la sesión el lobby en formato string
     */
    private void broadcast() {
        // TODO find out https://www.baeldung.com/gson-exclude-fields-serialization
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(gson.toJson(lobby), result -> {
                if (result.getException() != null) {
                    try {
                        s.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        });
    }
}
