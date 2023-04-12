package edu.eci.arsw.websockets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.impl.InMemoryLobbyDAO;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.services.SessionService;
import edu.eci.arsw.shared.TetrisException;
import edu.eci.arsw.threads.GameSession;

@ServerEndpoint(value ="/game/{username}/{codigo}")
@ApplicationScoped
public class GameSocket {

    List<GameSession> games = new ArrayList<>();

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final LobbyService lobbyService = new LobbyService(new InMemoryLobbyDAO());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo){
        try{
            sessions.put(username, session);
            Lobby l = lobbyService.get(codigo);

            // if doesnt throws, the sessions is completed
            Map<String, Session> lobbySessions = SessionService.getSessions(l, sessions, true);
            GameSession game = new GameSession(l, lobbySessions, lobbyService);
            game.start();
            games.add(game);
        }catch (TetrisException e){
            System.out.println("Aún faltan jugadores para empezar la partida o no existe el lobby ");
        }


    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo, Throwable throwable) {
        List<GameSession> gSessions = games.stream().filter( g -> g.getCodigoLobby() == codigo).collect(Collectors.toList());
        if(gSessions.size() > 0){
            GameSession gs = gSessions.get(0);
            gs.dropUser(username);
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("codigo") int codigo){
        List<GameSession> gSessions = games.stream().filter( g -> g.getCodigoLobby() == codigo).collect(Collectors.toList());
        if(gSessions.size() > 0){
            GameSession gs = gSessions.get(0);
            gs.moveBlock(username, message);
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}