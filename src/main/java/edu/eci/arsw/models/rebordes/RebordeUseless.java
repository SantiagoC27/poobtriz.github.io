package edu.eci.arsw.models.rebordes;


/**
 * Si se completa una línea con este bloque, la línea no desaparecerá
 */
public class RebordeUseless extends Reborde{

	public RebordeUseless() {
		super("blue");
	}
	
	public boolean isBorrable() {
		return false;
	}

	public boolean borrarCercanos(){
		return false;
	}


	public boolean modifyShape() {
		return false;
	}
}
