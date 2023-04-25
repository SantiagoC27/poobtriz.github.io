package edu.eci.arsw.models;

import edu.eci.arsw.models.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Lobby {
    @Setter
    private int codigo;

    @Setter
    private Estado estado;

    @Getter
    private int filas;

    @Getter
    private int cols;

    @Getter
    private int velocity;

    private final List<Player> players;

    public Lobby(int codigo, int filas, int cols, int velocity){
        this.codigo = codigo;
        this.estado = Estado.CREATED;
        this.players = new ArrayList<>();
        this.filas = filas;
        this.cols = cols;
        this.velocity = velocity;

    }

    public Lobby(){
        players = new ArrayList<>();
    }

    public int getCode() {
        return codigo;
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

    public void removePlayer(int index) {
        players.remove(index);
    }

    public List<String> getColorsTableros(){
        List<String> colors = new ArrayList<>();
        for (Player p : players) {
            colors.add(p.getColorTablero());
        }
        return colors;
    }
}
