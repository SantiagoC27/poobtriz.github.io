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
    public static final String INVALID_SESSION = "La sesión no tiene a los integrantes del lobby";
}
