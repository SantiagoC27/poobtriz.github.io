package edu.eci.arsw.persistence.impl;

import java.util.*;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;

public class InMemoryLobbyDAO implements ILobbyDAO{

    private static List<Lobby> lobbies = new ArrayList<>();

    @Override
    public Lobby get(Lobby lobby) {
        for (Lobby localLobby : lobbies) {
            if (localLobby.getCode() == lobby.getCode()) {
                return lobby;
            }
        }
        return null;
    }

    @Override
    public void create(Lobby lobby) {
        lobbies.add(lobby);
    }

  
    
}
