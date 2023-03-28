package edu.eci.arsw.persistence.lobby;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Admin;
import org.springframework.stereotype.Service;

@Service
public class LobbyBasicDAO implements  ILobbyDAO{

    private final Lobby l = new Lobby(11111);
    public LobbyBasicDAO(){
        l.addPlayer(new Admin("Jaime", new Tablero(true, 1000, "red", 20, 10, null)));
    }
    @Override
    public Lobby get(int codigo) {
        return l;
    }
}
