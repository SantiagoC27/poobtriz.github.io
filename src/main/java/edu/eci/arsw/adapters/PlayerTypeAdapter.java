package edu.eci.arsw.adapters;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;

import java.io.IOException;

public class PlayerTypeAdapter extends TypeAdapter<Player> {

    @Override
    public void write(JsonWriter out, Player value) throws IOException {
        out.beginObject();
        out.name("nick").value(value.getNick());
        out.endObject();
    }

    @Override
    public Player read(JsonReader in) throws IOException {
        Player player = new Jugador();

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
        return player;
    }
}
