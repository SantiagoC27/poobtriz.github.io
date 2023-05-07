package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.websocket.Session;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.services.SessionService;
import edu.eci.arsw.shared.TetrisException;

public class GameSession extends Thread{
    private final LobbyService lobbyService;

    private Lobby lobby;
    private final Map<String, Session> sessions;

    private final GameThread gt;

    private final AtomicBoolean playersMoved;

    public GameSession(Lobby lobby, Map<String, Session> allSessions, LobbyService lobbyService) throws TetrisException {
        this.lobby = lobby;
        this.sessions = SessionService.getSessions(lobby, allSessions, true);
        this.playersMoved = new AtomicBoolean(false);
        this.lobbyService = lobbyService;
        gt =new GameThread(this.lobby, this.playersMoved);

    }

    @Override
    public void run(){
        this.gt.start();
        try {
            while (gt.isAlive()){
                    synchronized (playersMoved){
                        if (!playersMoved.get()) playersMoved.wait();
                        playersMoved.set(false);
                        broadcast();
                    }
            }
            System.out.println("Session finished");
            SessionService.closeSessions(lobby, sessions);
        } catch (Exception ignored) {}
    }

    public void moveBlock(String user, String movement){
        gt.moveBlock(user, movement);
        broadcast();
    }

    public int getCodigoLobby(){
        return this.lobby.getCodigo();
    }

    /**
     * Emite a los usuarios de la sesión el lobby en formato string
     */
    private void broadcast() {
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
