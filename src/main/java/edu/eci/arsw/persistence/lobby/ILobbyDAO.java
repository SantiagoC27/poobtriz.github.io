package edu.eci.arsw.persistence.lobby;

import edu.eci.arsw.models.Lobby;

public interface ILobbyDAO {
    Lobby get(int codigo);

    Lobby removePlayer(Lobby lobby, String user);
}
