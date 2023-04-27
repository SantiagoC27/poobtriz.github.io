package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.eci.arsw.adapters.PlayerTypeAdapter;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.services.LobbyService;

public class GameSession extends Thread{
    private static final Gson gson = new GsonBuilder()
    //.registerTypeAdapter(Player.class, new PlayerTypeAdapter())
    .create();
    private final LobbyService lobbyService;

    private Lobby lobby;
    private final Map<String, Session> sessions;

    private final GameThread gt;

    private final AtomicBoolean playersMoved;

    public GameSession(Lobby lobby, Map<String, Session> allSessions, LobbyService lobbyService){
        this.lobby = lobby;
        this.sessions = allSessions;
        this.playersMoved = new AtomicBoolean(false);
        this.lobbyService = lobbyService;
        gt =new GameThread(this.lobby, this.playersMoved);

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
        System.out.println("Desde el session el game murio");

    }

    public void moveBlock(String user, String movement){
        this.gt.moveBlock(user, movement);
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
        List<String> failed = new ArrayList<>();
        sessions.forEach((key, value) -> value.getAsyncRemote().sendObject(lobby.toString(), result -> {
            if (result.getException() != null) failed.add(key);
        }));
        failed.forEach(this::dropUser);
    }

    /**
     * Eliminar un usuario del juego
     * @param user usuario
     */

    public void dropUser(String user){
        sessions.remove(user);
        lobby = lobbyService.removePlayer(lobby, user);
    }
}
