package edu.eci.arsw.services;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.persistence.PersistenceFactory;
import edu.eci.arsw.persistence.lobby.ILobbyDAO;
import edu.eci.arsw.shared.TetrisException;

public class LobbyService {

    private final ILobbyDAO lobbyDAO;

    public LobbyService(){
        this.lobbyDAO = PersistenceFactory.getLobbyDAO(true);
    }

    public Lobby get(int codigo) throws TetrisException{
        return this.lobbyDAO.get(codigo);
    }

    public Lobby removePlayer(Lobby lobby, String user) {
        return this.lobbyDAO.removePlayer(lobby, user);
    }

    public Lobby removePlayer(String user, int codigo){
        Lobby lobby = null;
        try{
            lobby = this.lobbyDAO.removePlayer(user, codigo);
        }catch (Exception ignored){}
        return lobby;
    }

    public Lobby addPlayer(String user, int codigo) throws TetrisException {
        Jugador p = new Jugador(user, null);
        return this.lobbyDAO.addPlayer(p, codigo);
    }

    public void create(Lobby lobby) {
        this.lobbyDAO.create(lobby);
    }

    public boolean isAdmin(String username, int codigo) {
        try{
            return this.lobbyDAO.get(codigo).getAdmin().getNick().equals(username);
        }catch (Exception ex){ ex.printStackTrace();}
        return false;
    }
}
