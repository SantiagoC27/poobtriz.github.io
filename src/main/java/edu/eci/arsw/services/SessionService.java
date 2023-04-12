package edu.eci.arsw.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.shared.TetrisException;

public class SessionService {
        /**
     * Filtra dentro de allSessions las sesiones correspondientes a los miembros del lobby.
     * @param lobby lobby
     * @param allSessions Todas las sesiones del websocket
     * @param throwing si es verdadero, generará excepción si en el map de sesiones no están todos los usuarios
     * @return sesiones de integrantes del lobby
     */
    public static Map<String, Session> getSessions(Lobby lobby, Map<String, Session> allSessions, boolean throwing) throws TetrisException {
        Map<String, Session> session = new ConcurrentHashMap<>();

        for (Player p: lobby.getPlayers()) {
            System.out.println(p.getNick());
            Session aux = allSessions.get(p.getNick());
            if (aux != null) session.put(p.getNick(), aux);
            else if (throwing) throw new TetrisException(TetrisException.INVALID_SESSION);
        }
        return session;
    }
}
