package edu.eci.arsw.models.rebordes;

public class RebordeClassic extends Reborde{
	public RebordeClassic() {
		super("black");
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
