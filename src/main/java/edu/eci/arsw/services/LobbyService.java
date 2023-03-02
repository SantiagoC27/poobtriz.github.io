package edu.eci.arsw.services;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Admin;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.shared.TetrisException;

public class LobbyService {

    public Lobby createLobby(Admin admin){
        return null;
    }

    /**
     * Agrega a newUser al lobby con el código elegido
     * @param newUser
     * @param codigo
     * @throws TetrisException si la sala no existe
     */
    public void joinLobby(Jugador newUser, int codigo) throws TetrisException {

    }

    /**
     * Borra en el back la sala y todos los datos relacionada a esta
     * @param lobby
     */

    public void endLobby(Lobby lobby, Admin admin ){

    }

}
