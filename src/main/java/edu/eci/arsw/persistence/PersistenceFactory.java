package edu.eci.arsw.persistence;

import edu.eci.arsw.persistence.impl.InMemoryLobbyDAO;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;

public class PersistenceFactory {

    private static final ILobbyDAO lobbyDAO = new InMemoryLobbyDAO();

    public static ILobbyDAO getLobbyDAO(boolean singleton){
        if (!singleton) return new InMemoryLobbyDAO();
        else return lobbyDAO;

    }
}
