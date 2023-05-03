package edu.eci.arsw.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.player.Player;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.player.Jugador;

public class PlayerTypeAdapterTest {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Player.class,new PlayerTypeAdapter()).create();

    @Test
    public void read() {
        String sLobby = "{ \"codigo\": 123, \"players\": [ { \"nick\": \"TEST\", \"tablero\": { \"cols\": 0, \"filas\": 0, \"background\": [], \"bgReborde\": [], \"puntuacion\": 0 } } ], \"estado\": \"CREATED\" }";

        Lobby lobby = gson.fromJson(sLobby, Lobby.class);
        assertEquals(1, lobby.getPlayers().size());
        assertEquals(123, lobby.getCodigo());
        assertEquals("TEST", lobby.getPlayers().get(0).getNick());
        assertEquals(Estado.CREATED, lobby.getEstado());
    }

    @Test
    public void write() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(new Jugador("Testing", new Tablero()));
        lobby.addPlayer(new Jugador("Testing2", null));
        String sLobby = gson.toJson(lobby);
        assertTrue(sLobby.contains(lobby.getPlayers().get(0).getNick()));
        assertTrue(sLobby.contains(lobby.getPlayers().get(1).getNick()));
        assertTrue(sLobby.contains(String.valueOf(lobby.getCodigo())));
        gson.fromJson(sLobby, Lobby.class);
    }
}