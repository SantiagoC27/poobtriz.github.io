package edu.eci.arsw.persistence.tablero;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Player;

public interface ITableroDAO {
    public Tablero get(Lobby lobby, Player user);

    public void save(Tablero t);
}
