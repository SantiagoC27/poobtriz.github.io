package edu.eci.arsw;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 */
public class Log{
    public static String nombre="Log.txt";
    

	 /**
	 * Genera los arcihvos necesarios para guardar una puntuacion
	 */
    public static void registre(Exception e)
    {
        try
        {
            Logger logger=Logger.getLogger(nombre);
            logger.setUseParentHandlers(false);
            FileHandler file = new FileHandler(nombre,true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
            logger.log(Level.SEVERE,e.toString(),e);
            file.close();
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
    }
}