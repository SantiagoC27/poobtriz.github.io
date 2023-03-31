package edu.eci.arsw.models;

import edu.eci.arsw.models.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Lobby {
    private int codigo;

    @Setter
    private Estado estado;

    private List<Player> players;

    public Lobby(int codigo){
        this.codigo = codigo;
        this.estado = Estado.CREATED;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player p){
        this.players.add(p);
    }

    public boolean endGame(){
        boolean finished = true;
        for (Player p : players) {
            if(!p.hasFinished()) finished = false;
        }
        return finished;
    }

    @Override
    public String toString(){
        return null;

    }

    public void removePlayer(String user) {
        players.removeIf( p -> Objects.equals(p.getNick(), user));
    }
}
