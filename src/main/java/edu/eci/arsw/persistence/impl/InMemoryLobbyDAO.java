package edu.eci.arsw.persistence.impl;

import java.util.*;


import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Admin;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.shared.TetrisException;

public class InMemoryLobbyDAO implements ILobbyDAO{

    private static List<Lobby> lobbies = new ArrayList<>();

    public InMemoryLobbyDAO(){
        Lobby l =new Lobby(123, 15, 10, 1000);
        l.addPlayer(new Admin("TEST", new Tablero(true, 1000, "red", 15, 10, null, null, new ArrayList<>())));
        lobbies.add(l);
    }

    @Override
    public Lobby get(int lobby) throws TetrisException {
        for (Lobby localLobby : lobbies) {
            if (localLobby.getCode() == lobby) {
                return localLobby;
            }
        }
        System.out.println(lobby);
        throw new TetrisException("");
    }

    @Override
    public void create(Lobby lobby) {
        System.out.println("Entro");
        Player admin = lobby.getPlayers().get(0);
        lobby.removePlayer(0);
        lobby.addPlayer(new Admin(admin.getNick(), null));
        lobbies.add(lobby);
    }

    @Override
    public Lobby removePlayer(Lobby lobby, String user){
        lobby.removePlayer(user);
        return lobby;
    }

    @Override
    public Lobby addPlayer(Jugador p, int codigo) throws TetrisException{
        Lobby lobby = get(codigo);
        if(lobby.getPlayers().stream().noneMatch(x->Objects.equals(x.getNick(), p.getNick())))
        {
            lobby.addPlayer(p);
        }
        return lobby;
    }

  
    
}
