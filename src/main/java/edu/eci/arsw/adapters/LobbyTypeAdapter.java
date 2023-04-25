package edu.eci.arsw.adapters;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Admin;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LobbyTypeAdapter extends TypeAdapter<Lobby> {

    @Override
    public void write(JsonWriter out, Lobby lobby) throws IOException {
        out.beginObject();
        out.name("codigo").value(lobby.getCodigo());
        out.name("players").beginArray();
        for (Player player : lobby.getPlayers()) {
            out.beginObject();
            out.name("name").value(player.getNick());
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Lobby read(JsonReader in) throws IOException {
        Lobby lobby = new Lobby(0);
        Player player;

        List<Player> players = new ArrayList<>();


        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "codigo":
                    lobby.setCodigo(in.nextInt());
                    break;
                case "estado":
                    lobby.setEstado(Estado.valueOf(in.nextString()));
                    break;
                case "players":
                    in.beginArray();
                    while (in.hasNext()) {
                            if (lobby.getPlayers().size() == 0) player = new Admin();
                            else player = new Jugador();
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "nick":
                                        player.setNick(in.nextString());
                                        break;
                                    default:
                                        in.skipValue();
                                        break;
                                }
                            }
                            in.endObject();
                            players.add(player);
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        for (Player p: players) {
            lobby.addPlayer(p);
        }
        return lobby;
    }
}
