package edu.eci.arsw.models.player;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.io.Serializable;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.shared.TetrisException;
import lombok.Setter;


public abstract class Player implements Serializable {
	private final String nick;
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

	public BloqueTetris getBlock() {
		return tablero.getBlock();
	}

	public Reborde getRebordeBg(int r, int c) {
		return tablero.bgReborde[r][c];
	}

	public boolean isUniform() {
		return Tablero.isUniforme();
	}

	public String getNick() {
		return nick;
	}


	public int getPuntuacionBLoq() {
		return tablero.getPuntuacionBloques();
	}

	public int getTiempo() {
		return tablero.getTiempo();
	}

	public int  getNumBuffs() {
		return tablero.getNumBuffs();
	}

	public void setVelocidad(int velInicial) {
		tablero.setVelocidad(velInicial);

	}

	public boolean moveBlockDown() {
		return tablero.moveBlockDown();
	}

	public void calculatePuntuacion(){
		tablero.calculatePuntuacion();
	}

	public boolean hasFinished() {
		return tablero.finGame();
	}
}
