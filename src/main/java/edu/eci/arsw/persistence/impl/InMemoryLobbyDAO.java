package edu.eci.arsw.persistence.impl;

import java.util.*;


import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.shared.TetrisException;

public class InMemoryLobbyDAO implements ILobbyDAO{

    private static List<Lobby> lobbies = new ArrayList<>();

    @Override
    public Lobby get(int lobby) throws TetrisException {
        for (Lobby localLobby : lobbies) {
            if (localLobby.getCode() == lobby) {
                return localLobby;
            }
        }
        throw new TetrisException("");
    }

    @Override
    public void create(Lobby lobby) {
        lobbies.add(lobby);
    }

  
    
}
