package edu.eci.arsw.adapters;


import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Jugador;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.models.rebordes.Reborde;

public class PlayerTypeAdapter extends TypeAdapter<Player> {
    private final Gson gson = new Gson();
    @Override
    public void write(JsonWriter out, Player value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("nick").value(value.getNick());
        out.name("tablero");
        out.beginObject();
        Tablero t = value.getTablero();
        if (t != null){
            out.name("filas").value(t.background.length);
            out.name("background");
            out.beginArray();
            for (String[] row : t.background) {
                out.beginArray();
                for (String col : row) {
                    out.value(col);
                }
                out.endArray();
            }
            out.endArray();

            out.name("bgReborde");
            out.beginArray();
            for (Reborde[] row : t.bgReborde) {
                out.beginArray();
                for (Reborde reborde : row) {
                    if (reborde != null){
                        out.beginObject();
                        out.name("color").value(reborde.getColor());
                        out.endObject();
                    }else out.nullValue();

                }
                out.endArray();
            }
            out.endArray();


        }
        out.endObject();

        out.endObject();

    }


    private void genMatrix(JsonWriter out, String[][] matrix) throws IOException {
        out.beginArray();
        for (String[] row : matrix) {
            out.beginArray();
            for (String col : row) {
                out.value(col);
            }
            out.endArray();
        }
        out.endArray();
    }

    @Override
    public Player read(JsonReader in) throws IOException {
        Player player = new Jugador();
        player.setTablero(new Tablero());
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "nick":
                    player.setNick(in.nextString());
                    break;
                case "tablero":
                    in.beginObject();
                    while (in.hasNext()){
                     String tableroAttribute = in.nextName();
                        
                     if (tableroAttribute.equals("velocidad")) {
                         player.getTablero().setVelocidad(in.nextInt());
                    }

                    else in.skipValue();
                        
                    }
                     in.endObject();
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
