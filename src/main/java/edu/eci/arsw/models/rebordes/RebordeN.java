package edu.eci.arsw.models.rebordes;
import java.awt.Color;
public class RebordeN extends Reborde{
	//classic
	public RebordeN() {
		super(Color.black);
		super.color = Color.black;
	}

	public boolean isBorrable() {
		return true;
	}
	public boolean borrarCercanos(){
		return false;
	}

	public boolean modifyShape() {
		return false;
	}
}
