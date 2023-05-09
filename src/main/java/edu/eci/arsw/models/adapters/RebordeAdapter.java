package edu.eci.arsw.models.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.models.rebordes.RebordeClassic;

import java.io.IOException;

public class RebordeAdapter extends TypeAdapter<Reborde> {

    @Override
    public void write(JsonWriter out, Reborde reborde) throws IOException {
        out.value(1);
        // No necesitas implementar este método si solo deseas convertir de JSON a objeto
    }

    @Override
    public Reborde read(JsonReader in) throws IOException {
        Reborde reborde = null;
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("color")) {
                in.nextString();
                reborde = new RebordeClassic(); // Aquí debes proporcionar la lógica para crear la instancia de la clase Reborde con el color correspondiente
            } else {
                in.skipValue(); // Saltar otros atributos desconocidos
            }
        }
        in.endObject();
        return reborde;
    }
}