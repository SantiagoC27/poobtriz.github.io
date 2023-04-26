package edu.eci.arsw.models.player;

import java.io.Serializable;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.shared.TetrisException;
import lombok.Setter;



public abstract class Player implements Serializable {
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

	public Player(){
		this.nick = "";
		this.tablero = null;
	}

	public String getNick() {
		return nick;
	}

	public boolean moveBlock(String movement) throws TetrisException {
		return tablero.moveBlock(movement.toUpperCase());
	}

	public boolean hasFinished() {
		if (tablero != null)
			return tablero.hasFinished();
		return true;
	}

	@Override
	public String toString() {
		return String.format("{\"nick\": \"%s\"}", nick);
	}
}
