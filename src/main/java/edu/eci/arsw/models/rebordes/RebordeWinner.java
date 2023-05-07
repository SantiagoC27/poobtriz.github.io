package edu.eci.arsw.models.rebordes;


/**
 * Se transforman en en el mejor bloque para el lugar donde se ubican
 */
public class RebordeWinner extends Reborde{

	public RebordeWinner() {
		super("#efb810");
	}
	
	public boolean isBorrable() {
		return true;
	}

	public boolean borrarCercanos(){
		return false;
	}



	public boolean modifyShape() {
		return true;
	}
	
}
