package edu.eci.arsw.models.rebordes;
import java.awt.Color;
public class RebordeD extends Reborde{
	//winner
	public RebordeD() {
		super(Color.decode("#efb810"));
		super.color = Color.decode("#efb810");
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
