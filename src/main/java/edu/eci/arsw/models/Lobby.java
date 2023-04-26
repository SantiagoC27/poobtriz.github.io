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
