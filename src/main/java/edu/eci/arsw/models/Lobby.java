package edu.eci.arsw.models;

import edu.eci.arsw.models.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private int codigo;
    @Getter
    private Estado estado;

    @Getter
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
        // TODO
        return false;
    }

    @Override
    public String toString(){
        return null;

    }
}
