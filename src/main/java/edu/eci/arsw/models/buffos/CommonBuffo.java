package edu.eci.arsw.models.buffos;

import edu.eci.arsw.models.Tablero;

import java.util.List;

public class CommonBuffo {
    // Clase creada para que los tableros tengan en comun un objeto con misma direccion de memoria que controle el buffo
    private Buffo buffo;

    public Buffo get(){
        return buffo;
    }

    public void set(Buffo buffo){
        System.out.println(buffo);
        this.buffo = buffo;
    }

    public int getY(){
        return buffo.getY();
    }

    public int getX(){
        return buffo.getX();
    }

    public void activate(List<Tablero> tableros, int idTablero){
        buffo.activate(tableros, idTablero);
    }
}
