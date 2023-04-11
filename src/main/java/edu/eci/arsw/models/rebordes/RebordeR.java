package edu.eci.arsw.models.rebordes;

public class RebordeR extends Reborde {
	//bomb
	public RebordeR() {
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
