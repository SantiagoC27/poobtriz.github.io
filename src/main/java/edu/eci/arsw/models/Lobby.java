package edu.eci.arsw.models;

import edu.eci.arsw.models.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Lobby {
    private int codigo;

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
}
