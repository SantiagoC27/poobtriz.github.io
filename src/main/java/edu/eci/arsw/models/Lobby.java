package edu.eci.arsw.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.eci.arsw.models.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Lobby {
    @Setter
    private int codigo;

    @Setter
    private Estado estado;

    private int filas;

    private int cols;

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
        if (!this.players.stream().anyMatch(player -> player.getNick().equals(p.getNick())))
            this.players.add(p);
    }
    
    public boolean endGame(){
        boolean finished = true;
        for (Player p : players) {
            if(!p.hasFinished()) finished = false;
        }
        return finished;
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
    
    @Override
    public String toString() {

        StringBuilder rta = new StringBuilder(String.format("{\"codigo\": %d, \"players\": [", codigo));
        for (int i = 0; i < players.size(); i++) {
            rta.append(players.get(i).toString());
            if (i != players.size()-1) rta.append(",");
        }

        rta.append("]}");
        return rta.toString();
    }
}
