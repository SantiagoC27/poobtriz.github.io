package edu.eci.arsw.websockets;

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
import edu.eci.arsw.persistence.lobby.LobbyBasicDAO;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.shared.TetrisException;
import edu.eci.arsw.threads.GameSession;

@ServerEndpoint(value ="/lobby/{username}/{codigo}")
@ApplicationScoped
public class LobbySocket {
    private static final Gson gson = new Gson();

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final LobbyService lobbyService = new LobbyService(new LobbyBasicDAO());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo){
        try{
            sessions.put(username, session);
            System.out.println("=====================");
            Lobby l = lobbyService.addPlayer(username, codigo);
            System.out.println(sessions.size());
            Map<String, Session> lobbySessions = GameSession.getSessions(l, sessions, false);
            System.out.println(lobbySessions.size());
            
            broadcast(gson.toJson(l), lobbySessions);
        }catch (TetrisException ignored){}


    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo) {
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo, Throwable throwable) {
        System.out.println("Error" + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("codigo") int codigo){

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