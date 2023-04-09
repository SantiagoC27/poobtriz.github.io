package edu.eci.arsw.models;

import edu.eci.arsw.models.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private int codigo;
    private Estado estado;
    private List<Player> players;

    public Lobby(int codigo){
        this.codigo = codigo;
        this.estado = Estado.CREATED;
        this.players = new ArrayList<>();
    }

    public int getCode() {
        return codigo;
    }

    public void setCode(int codigo) {
        this.codigo = codigo;
    }

    public Estado getStatus() {
        return estado;
    }

    public void setStatus(Estado estado) {
        this.estado = estado;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
