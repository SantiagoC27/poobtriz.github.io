package edu.eci.arsw.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LobbyTypeAdapter extends TypeAdapter<Lobby> {

    @Override
    public void write(JsonWriter out, Lobby lobby) throws IOException {
        out.beginObject();
        out.name("codigo").value(lobby.getCodigo());
        out.name("estado").value(lobby.getEstado().toString());
        out.name("players").beginArray();
        for (Player player : lobby.getPlayers()) {
            out.value(player.getClass().getName());
            out.beginObject();
            out.name("name").value(player.getNick());
            // Aquí puedes agregar más campos específicos de Player si los tienes
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Lobby read(JsonReader in) throws IOException {
        Lobby lobby = new Lobby();
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
                        String className = in.nextString();
                        try {
                            Class<?> playerClass = Class.forName(className);
                            Player player = (Player) playerClass.newInstance();
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "nick":
                                        player.setNick(in.nextString());
                                        break;
                                    // Aquí puedes agregar más campos específicos de Player si los tienes
                                    default:
                                        in.skipValue();
                                        break;
                                }
                            }
                            in.endObject();
                            players.add(player);
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
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
