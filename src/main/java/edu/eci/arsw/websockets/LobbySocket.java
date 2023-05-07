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

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.services.SessionService;
import edu.eci.arsw.shared.TetrisException;

@ServerEndpoint(value ="/lobby/{username}/{codigo}")
@ApplicationScoped
public class LobbySocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final LobbyService lobbyService = new LobbyService();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo){
        try{
            sessions.put(username, session);
            Lobby l = lobbyService.addPlayer(username, codigo);
            Map<String, Session> lobbySessions = SessionService.getSessions(l, sessions, false);
            
            broadcast(l.toString(), lobbySessions);
        }catch (TetrisException e){ e.printStackTrace();}


    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo) throws IOException, TetrisException {
        SessionService.closeSessions(lobbyService.get(codigo), sessions);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo, Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("Error" + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("codigo") int codigo) throws TetrisException {
        Lobby lobby = lobbyService.removePlayer(message, codigo);
        sessions.remove(message);
        Map<String, Session> lobbySessions = SessionService.getSessions(lobby, sessions, false);
        broadcast(lobby.toString(), lobbySessions);
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