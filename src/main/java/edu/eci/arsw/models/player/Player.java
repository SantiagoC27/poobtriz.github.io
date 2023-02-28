package edu.eci.arsw.models.player;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.io.Serializable;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.rebordes.Reborde;

public abstract class Player implements KeyListener, Serializable {
	private String nick;
	protected Tablero t;
	
	public Player(String name, Tablero t) {
		this.nick = name;
		this.t = t;
	}
	

	 /**
	 * Empieza el juego
	 */
	public abstract void run();

	public BloqueTetris getBlock() {
		return t.getBlock();
	}
	
	public Color getColorBg(int r, int c){
		return t.background[r][c];
	}

	public Color getColorBg() {
		return t.getBg();
	}

	public boolean isAlive() {
		return t.isAlive();
	}

	public Reborde getRebordeBg(int r, int c) {
		return t.bgReborde[r][c];
	}

	public boolean isUniform() {
		return Tablero.isUniforme();
	}
	
	public String getNick() {
		return nick;
	}

	
	public int getPuntuacionBLoq() {
		return t.getPuntuacionBloques();
	}

	public int getTiempo() {
		return t.getTiempo();
	}
	public abstract void registrePuntuacion();

	public void end() {
		t.interrupt();
		
	}
	
	public int  getNumBuffs() {
		return t.getNumBuffs();
	}

	public void setVelocidad(int velInicial) {
		t.setVelocidad(velInicial);
		
	}
	
	public Tablero getTablero() {
		return t;
	}
}
