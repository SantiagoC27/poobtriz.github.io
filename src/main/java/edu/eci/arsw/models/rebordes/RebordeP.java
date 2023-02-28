package edu.eci.arsw.models.rebordes;
import java.awt.Color;

public class RebordeP extends Reborde{
	//useless
	public RebordeP() {
		super(new Color(192, 192, 192));
		this.color = new Color(192, 192, 192); 
		
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
