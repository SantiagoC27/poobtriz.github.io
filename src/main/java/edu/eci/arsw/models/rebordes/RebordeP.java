package edu.eci.arsw.models.rebordes;
import java.awt.Color;

public class RebordeP extends Reborde{
	//useless
	public RebordeP() {
		super("blue");
		this.color = "blue";
		
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
