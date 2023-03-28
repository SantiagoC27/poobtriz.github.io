package edu.eci.arsw.services;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.persistence.lobby.LobbyBasicDAO;
import edu.eci.arsw.persistence.tablero.ITableroDAO;
import edu.eci.arsw.shared.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TetrisService implements Runnable{

    private final ILobbyDAO lobbyDAO;
    private final ITableroDAO tableroDAO;

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Autowired
    public TetrisService(ILobbyDAO lobbyDAO, ITableroDAO tableroDAO){
        this.lobbyDAO = lobbyDAO;
        this.tableroDAO = tableroDAO;
    }

    public TetrisService(Map<String, Session> sessions){
        this.lobbyDAO = new LobbyBasicDAO();
        this.tableroDAO = null;
        this.sessions = sessions;
    }

    public void moveDown(Player user){
        try {

            Tablero t = null;
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockDown();
        } catch (Exception e) {
            Log.registre(e);
        }
    }

    public Lobby moveRigth(int codigo, Player user){
        Lobby lobby = lobbyDAO.get(codigo);
        try {

            Tablero t = tableroDAO.get(lobby, user);
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockRight();
        } catch (Exception e) {
            Log.registre(e);
        }

        return lobby;
    }


    public Lobby moveLeft(int codigo, Player user){
        Lobby lobby = lobbyDAO.get(codigo);
        try {
            Tablero t = tableroDAO.get(lobby, user);
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockLeft();
            tableroDAO.save(t);
        } catch (Exception e) {
            Log.registre(e);
        }
        return lobby;
    }

    @Override
    public void run() {
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


    private void instanceGame(Lobby lobby){
        ConcurrentLinkedQueue<BloqueTetris> bloques = new ConcurrentLinkedQueue<>();
        Tablero t;
        //Generar un tablero con la misma lista de bloques (atomica)
        for (Player player: lobby.getPlayers()) {
            t = new Tablero(true, 1000, "yellow", 20, 10, bloques);
            player.setTablero(t);
        }

    }
}
