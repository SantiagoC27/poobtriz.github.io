package edu.eci.arsw.models.player;

import java.io.Serializable;
import java.util.Objects;

import edu.eci.arsw.models.Tablero;
import lombok.Setter;



public class Player implements Serializable {
	@Setter
	private String nick;
	@Setter
	protected Tablero tablero;

	public Player(String name, Tablero t) {
		this.nick = name;
		this.tablero = t;
	}

	public Player(String name){
		this.nick = name;
		this.tablero = null;
	}

	public Tablero getTablero(){
		return this.tablero;
	}

	public Player(){
		this.nick = "";
		this.tablero = null;
	}

	public String getNick() {
		return nick;
	}

	public String getColorTablero(){
		String color = null;
		if (tablero != null) color = tablero.getBg();
		return color;
	}

	public void moveBlock(String movement){
		try{
			synchronized (tablero){
				tablero.setMovilidadBlock(true);
				tablero.moveBlock(movement.toUpperCase());
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public boolean hasFinished() {
		boolean finished = true;
		if (tablero != null)
			finished = tablero.hasFinished();
		return finished;
	}

	public int getVelocity(){
		return tablero.getVelocidad();
	}

	@Override
	public String toString() {
		StringBuilder sRta = new StringBuilder();
		sRta.append(String.format("{\"nick\": \"%s\"", nick));
		if (tablero != null){
			sRta.append(String.format(",\"tablero\": %s", tablero));
		}
		sRta.append("}");
		return sRta.toString();
	}

	public boolean isEmptyCoord(int[] coord) {
		return Objects.equals(tablero.background[coord[0]][coord[1]], tablero.getBg());
	}
}
