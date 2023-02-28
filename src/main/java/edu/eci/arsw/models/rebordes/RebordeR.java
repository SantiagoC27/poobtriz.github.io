package edu.eci.arsw.models.rebordes;

import java.awt.Color;

public class RebordeR extends Reborde {
	//bomb
	public RebordeR() {
		super(Color.red);
		super.color = Color.red;
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
