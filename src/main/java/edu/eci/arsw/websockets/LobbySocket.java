package edu.eci.arsw.websockets;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.services.SessionService;
import edu.eci.arsw.shared.TetrisException;

@ServerEndpoint(value ="/lobby/{username}/{codigo}")
@ApplicationScoped
public class LobbySocket {
    private static final Gson gson = new Gson();

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final LobbyService lobbyService = new LobbyService();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo){
        try{
            sessions.put(username, session);
            Lobby l = lobbyService.addPlayer(username, codigo);
            Map<String, Session> lobbySessions = SessionService.getSessions(l, sessions, false);
            
            broadcast(gson.toJson(l), lobbySessions);
        }catch (TetrisException ignored){}


    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo) throws IOException, TetrisException {

        Map<String, Session> lobbySessions = SessionService.getSessions(lobbyService.get(codigo), sessions, false);
        for (String user:  lobbySessions.keySet()) {
            lobbySessions.get(user).close();
            sessions.remove(user);
        }
        sessions.remove(username);

    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo, Throwable throwable) {
        System.out.println("Error" + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("codigo") int codigo) throws TetrisException {
        Lobby lobby = lobbyService.removePlayer(message, codigo);
        sessions.remove(message);
        Map<String, Session> lobbySessions = SessionService.getSessions(lobby, sessions, false);
        broadcast(gson.toJson(lobby), lobbySessions);
    }

    private void broadcast(String message, Map<String, Session> sessionsLobby) {
        sessionsLobby.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}