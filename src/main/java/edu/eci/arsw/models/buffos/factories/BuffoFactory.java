package edu.eci.arsw.models.buffos.factories;

import edu.eci.arsw.models.buffos.Buffo;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

public class BuffoFactory {

    private static final List<Class<? extends Buffo>> buffosSubclasses = findBuffoSubclasses();

    public static Buffo getRandomBuffo(int[] c, List<String> colors) {
        Buffo b = null;
        if (buffosSubclasses.isEmpty()) throw new IllegalStateException("No se encontraron subclases de Buffo");

        int randomIndex = (int) (Math.random() * buffosSubclasses.size());
        Class<? extends Buffo> selectedBuffoClass = buffosSubclasses.get(randomIndex);

        try {
            b = selectedBuffoClass.getDeclaredConstructor(int[].class).newInstance(c);
            if (colors.contains(b.getColor())) b = getRandomBuffo(c, colors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    private static List<Class<? extends Buffo>> findBuffoSubclasses() {
        Reflections reflections = new Reflections("edu.eci.arsw.models.buffos");

        return new ArrayList<>(reflections.getSubTypesOf(Buffo.class));
    }

}
