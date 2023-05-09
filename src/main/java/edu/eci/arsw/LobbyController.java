package edu.eci.arsw;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.adapters.RebordeAdapter;
import edu.eci.arsw.models.rebordes.Reborde;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.shared.TetrisException;


@Path("/lobbies")
public class LobbyController {
    LobbyService lobbyS = new LobbyService();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Reborde.class, new RebordeAdapter())
            .create();


    @GET
    @Path("/{lobby}")
    public Response getLobby(@PathParam(value = "lobby") int lobby) throws TetrisException {
    Lobby lobbyObtenido = lobbyS.get(lobby);
    if (lobbyObtenido == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Lobby no encontrado").build();
    }
    return Response.ok(lobbyObtenido).build();
    }

    @POST
    public Lobby crearLobby(String sLobby) {
        Lobby lobby = gson.fromJson(sLobby, Lobby.class);
        lobbyS.create(lobby);
        return lobby;
    }
     
}
