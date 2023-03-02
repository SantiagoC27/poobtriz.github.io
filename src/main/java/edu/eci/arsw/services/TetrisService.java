package edu.eci.arsw.services;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.persistence.tablero.ITableroDAO;
import edu.eci.arsw.shared.Log;
import edu.eci.arsw.shared.TetrisException;
import org.springframework.beans.factory.annotation.Autowired;

public class TetrisService {


    private final ILobbyDAO lobbyDAO;
    private final ITableroDAO tableroDAO;

    @Autowired
    public TetrisService(ILobbyDAO lobbyDAO,  ITableroDAO tableroDAO){
        this.lobbyDAO = lobbyDAO;
        this.tableroDAO = tableroDAO;
    }

    public Lobby moveDown(Lobby lobby, Player user){

        try {
            Tablero t = tableroDAO.get(lobby, user);
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockDown();
            tableroDAO.save(t);
            lobby = lobbyDAO.get(lobby);
        } catch (TetrisException e) {
            Log.registre(e);
        }

        return lobby;
    }

    public Lobby moveRigth(Lobby lobby, Player user){

        try {
            Tablero t = tableroDAO.get(lobby, user);
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockRight();
            tableroDAO.save(t);
            lobby = lobbyDAO.get(lobby);
        } catch (Exception e) {
            Log.registre(e);
        }

        return lobby;
    }


    public Lobby moveLeft(Lobby lobby, Player user){
        try {
            Tablero t = tableroDAO.get(lobby, user);
            if (t.getBlock() == null)
                t.spawnBlock();
            t.moveBlockLeft();
            tableroDAO.save(t);
            lobby = lobbyDAO.get(lobby);
        } catch (Exception e) {
            Log.registre(e);
        }

        return lobby;
    }

}
