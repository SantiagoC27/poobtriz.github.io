package edu.eci.arsw.shared;

/**
 * class TetrisException .
 * 
 * @authors Juan Pablo Fonseca Cardenas, Santiago Cardenas 
 * @version 1.0
   */
public class TetrisException extends Exception
{
    public TetrisException(String _message)
    {
        super(_message);
    }
    
    public final static String BLOCK_NULL = "No hay bloque";
	public static final String END_TABLERO = "La figura ha llegado al final del tablero";
	public static final String PROBLEM_SAVE_FILE = "Hubo un problema al guardar el juego";
	public static final String PROBLEM_OPEN_FILE = "Hubo un problema al abrir el juego";
	public static final String INVALID_USER = "El usuario no existe, por favor digite un user valido";
}
