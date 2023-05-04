package edu.eci.arsw.models.buffos.factories;

import edu.eci.arsw.models.buffos.Buffo;
import edu.eci.arsw.models.buffos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuffoFactory {
    
    private static final List<String> posiblesBuffos = llenarPosiblesBuffos();

    /**
     * Selecciona un buffo aleatorio en una lista que los contenga
     * @param c coordenada del buffo x,y
     *
     * @return el buffo aleatorio
     */
    public static Buffo getRandomBuffo(int[] c, List<String> colors) {
        Buffo b = null;
        if(posiblesBuffos.size() != 0 && c != null) {
            Random random = new Random();
            int n = random.nextInt(posiblesBuffos.size());
            String tipo = posiblesBuffos.get(n);
            switch (tipo.toUpperCase()) {
                case "SD":
                    b = new BuffoSD(c);
                    break;
                case "ST":
                    b = new BuffoST(c);
                    break;
                case "X":
                    b = new BuffoX(c);
                    break;
                default:
                    b = new BuffoS(c);
                    break;
            }
            if (colors.contains(b.getColor())) b = getRandomBuffo(c, colors);
        }
        return b;
    }
    public static List<String> llenarPosiblesBuffos(){
        List<String> lista =new ArrayList<>();
        lista.add("SD");
        lista.add("ST");
        lista.add("X");
        lista.add("NORMAL");
        return lista;
    }

}
