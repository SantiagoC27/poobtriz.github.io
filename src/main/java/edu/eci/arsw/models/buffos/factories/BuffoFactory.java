package edu.eci.arsw.models.buffos.factories;

import edu.eci.arsw.models.buffos.*;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

public class BuffoFactory {

    private static final List<Class<? extends Buffo>> buffosSubclasses = findBuffoSubclasses();

    public static Buffo getRandomBuffo(int[] c) {
        Buffo b = null;
        if (buffosSubclasses.isEmpty()) throw new IllegalStateException("No se encontraron subclases de Buffo");

        int randomIndex = (int) (Math.random() * buffosSubclasses.size());
        Class<? extends Buffo> selectedBuffoClass = buffosSubclasses.get(randomIndex);

        try {
            b = selectedBuffoClass.getDeclaredConstructor(int[].class).newInstance(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    public static Buffo getRandomBuffo() {
        return new BuffoFaster(new int[]{2,3});
    }

    private static List<Class<? extends Buffo>> findBuffoSubclasses() {
        Reflections reflections = new Reflections("edu.eci.arsw.models.buffos");

        return new ArrayList<>(reflections.getSubTypesOf(Buffo.class));
    }

}
