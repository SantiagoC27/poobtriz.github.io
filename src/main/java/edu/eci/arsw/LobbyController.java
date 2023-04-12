package edu.eci.arsw;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.persistence.impl.InMemoryLobbyDAO;
import edu.eci.arsw.services.LobbyService;
import edu.eci.arsw.shared.TetrisException;


@Path("/lobbies")
public class LobbyController {

    InMemoryLobbyDAO dao = new InMemoryLobbyDAO();
    LobbyService lobbyS = new LobbyService(dao);

    

    @GET
    @Path("/{lobby}")
    public Response getLobby(@PathParam(value = "lobby") int lobby) throws TetrisException {
        System.out.println("ENTRO");
    Lobby lobbyObtenido = lobbyS.get(lobby);
    if (lobbyObtenido == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Lobby no encontrado").build();
    }
    return Response.ok(lobbyObtenido).build(); 
    }

    @POST
    @Path("/")
    public Lobby crearLobby(Lobby lobby) {
        System.out.println(lobby);
        if (lobby.getPlayers().size() < 1) {
            throw new WebApplicationException("Debe haber al menos un jugador en el lobby", 400);
        }
        lobbyS.create(lobby);
        return lobby;
    }
     
}
