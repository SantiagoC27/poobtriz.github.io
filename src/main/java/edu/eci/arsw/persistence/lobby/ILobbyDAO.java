package edu.eci.arsw.persistence.lobby;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.shared.TetrisException;

public interface ILobbyDAO {
    Lobby get(int lobby) throws TetrisException;
    void create(Lobby lobby);
}
