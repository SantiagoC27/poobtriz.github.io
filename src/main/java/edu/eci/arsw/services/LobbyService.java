package edu.eci.arsw.services;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.shared.TetrisException;

public class LobbyService {

    private final ILobbyDAO lobbyDAO;

    public LobbyService(ILobbyDAO lobbyDAO){
        this.lobbyDAO = lobbyDAO;
    }

    public Lobby get(int codigo) throws TetrisException{
        return this.lobbyDAO.get(codigo);
    }

    public Lobby removePlayer(Lobby lobby, String user) {
        return this.lobbyDAO.removePlayer(lobby, user);
    }

    public Lobby addPlayer(String user, int codigo) throws TetrisException {
        Jugador p = new Jugador(user, null);
        return this.lobbyDAO.addPlayer(p, codigo);
    }

    public void create(Lobby lobby) {
        this.lobbyDAO.create(lobby);
    }
}
