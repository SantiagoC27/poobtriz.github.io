package edu.eci.arsw.services;

import java.io.IOException;
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
            Session aux = allSessions.get(p.getNick());

            if (aux != null) session.put(p.getNick(), aux);
            else if (throwing) throw new TetrisException(TetrisException.INVALID_SESSION);
        }
        return session;
    }

    public static void closeSessions(Lobby lobby, Map<String, Session> sessions) throws TetrisException, IOException {
        Map<String, Session> lobbySessions = SessionService.getSessions(lobby, sessions, false);
        for (String user:  lobbySessions.keySet()) {
            lobbySessions.get(user).close();
            sessions.remove(user);
        }
    }
}
