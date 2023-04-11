package edu.eci.arsw.persistence.lobby;

import org.springframework.stereotype.Service;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Admin;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.shared.TetrisException;

@Service
public class LobbyBasicDAO implements  ILobbyDAO{

    private final Lobby l = new Lobby(11111);
    public LobbyBasicDAO(){
        l.addPlayer(new Admin("Jaime", new Tablero(true, 1000, "red", 15, 10, null)));
    }
    @Override
    public Lobby get(int codigo) throws TetrisException{
        return l;
    }

    @Override
    public Lobby removePlayer(Lobby lobby, String user){
        lobby.removePlayer(user);
        return lobby;
    }

    @Override
    public Lobby addPlayer(Jugador p, int codigo) throws TetrisException{
        Lobby lobby = get(codigo);
        lobby.addPlayer(p);
        return lobby;
    }
}

