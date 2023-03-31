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

import com.google.gson.Gson;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.lobby.LobbyBasicDAO;
import edu.eci.arsw.threads.GameSession;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.services.TetrisService;
import edu.eci.arsw.shared.TetrisException;
import edu.eci.arsw.threads.GameThread;
import org.jboss.logging.Logger;

@ServerEndpoint(value ="/game/{username}/{codigo}")
@ApplicationScoped
public class GameSocket {

    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(GameSocket.class);

    List<GameSession> games = new ArrayList<>();

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final TetrisService tetrisService = new TetrisService(sessions);

    private final LobbyService lobbyService = new LobbyService(new LobbyBasicDAO());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo){
        try{
            sessions.put(username, session);
            Lobby l = lobbyService.get(codigo);

            // if doesnt throws, the sessions is completed
            Map<String, Session> lobbySessions = GameSession.getSessions(l, sessions);
            GameSession game = new GameSession(l, lobbySessions);
            game.start();
            games.add(game);
        }catch (TetrisException e){

        }


    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("codigo") int codigo, Throwable throwable) {
        sessions.remove(username);
        LOG.error("onError", throwable);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("codigo") int codigo){
        GameSession gs =games.stream().filter( g -> g.getCodigoLobby() == codigo).collect(Collectors.toList()).get(0);
        gs.moveBlock(username, message);

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