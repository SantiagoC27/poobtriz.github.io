package edu.eci.arsw.persistence.lobby;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.shared.TetrisException;

public interface ILobbyDAO {
    Lobby get(int codigo) throws TetrisException;

    Lobby removePlayer(Lobby lobby, String user);

    Lobby removePlayer(String user, int codigo) throws TetrisException;

    Lobby addPlayer(Jugador p, int codigo) throws TetrisException;

    void create(Lobby lobby);
}
