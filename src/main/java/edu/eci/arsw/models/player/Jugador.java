package edu.eci.arsw.models.player;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.Log;


public class Jugador extends Player{
	private static int cont = 1;
	private int id;
	public Jugador( String name, Tablero t) {
		super(name, t);
		id = cont;
		Jugador.cont++;
	}


	public void run() {
		try {
			t.start();
		}catch(Exception e) {
			Log.registre(e);
		}
	}


	/**
	 * Valida la puntuacion alcanzada 
	 */

	@Override
	public void registrePuntuacion() {
		if(Persistencia.huboRecord(getNick(), getPuntuacionBLoq())) 
			JOptionPane.showConfirmDialog(null, "Felicidades has logrado un record personal", "Warning",JOptionPane.PLAIN_MESSAGE);
		Persistencia.registrePuntuacion(getNick(), this);

		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
	}



	 /**
	 * Confugura los controles para 1 o 2 jugadores
	 * @param la tecla presionada
	 */

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			if( e.getKeyCode() == KeyEvent.VK_P) {Tablero.pausar();}
			if(id == 1) {
				if( e.getKeyCode() == KeyEvent.VK_D) {t.moveBlockRight();}
				else if(e.getKeyCode() == KeyEvent.VK_A) {t.moveBlockLeft();}
				else if(e.getKeyCode() == KeyEvent.VK_W) {t.rotarBlock();}
				else if(e.getKeyCode() == KeyEvent.VK_S) {
					t.setMovilidadBlock(true);
					t.moveBlockDown();}		
			}else {
				if( e.getKeyCode() == KeyEvent.VK_RIGHT) {t.moveBlockRight();}
				else if(e.getKeyCode() == KeyEvent.VK_LEFT) {t.moveBlockLeft();}
				else if(e.getKeyCode() == KeyEvent.VK_UP) {t.rotarBlock();}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					t.setMovilidadBlock(true);
					t.moveBlockDown();}
			}
		}catch (Exception e1) {}


		
	}

}
