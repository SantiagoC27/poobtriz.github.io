package edu.eci.arsw.models.rebordes;


/**
 * Cuando el bloque se fije se autodestruirá y destruirán los bloques que este toque
 */
public class RebordeBomb extends Reborde {

	public RebordeBomb() {
		super("red");
	}
	
	public boolean isBorrable() {
		return true;
	}

	public boolean borrarCercanos(){
		return true;
	}


	public boolean modifyShape() {
		return false;
	}
}
