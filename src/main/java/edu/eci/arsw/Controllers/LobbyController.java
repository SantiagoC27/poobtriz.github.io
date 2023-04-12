package edu.eci.arsw.Controllers;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.impl.InMemoryLobbyDAO;
import edu.eci.arsw.shared.TetrisException;
@Path("/lobbies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LobbyController {

    InMemoryLobbyDAO dao;

    @POST
    public Lobby crearLobby(Lobby lobby) {
        if (lobby.getPlayers().size() < 1) {
            throw new WebApplicationException("Debe haber al menos un jugador en el lobby", 400);
        }
        //Falta establecer el administrador
        dao.create(lobby);
        return lobby;
    }

    @GET
    public Response getLobby(int lobby) throws TetrisException {
    Lobby lobbyObtenido = dao.get(lobby);
    if (lobbyObtenido == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Lobby no encontrado").build();
    }
    return Response.ok(lobbyObtenido).build(); 
    }

        
}
