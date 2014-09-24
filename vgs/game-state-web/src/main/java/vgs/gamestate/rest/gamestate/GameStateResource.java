package vgs.gamestate.rest.gamestate;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vgs.gamestate.entity.GameState;


@Path("/game-state")
public class GameStateResource {

    @Inject
    private vgs.gamestate.service.GameStateService service;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String root() {
        return "test";
    }

    @GET
    @Path("/{gameRoundId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameState getGameState(@PathParam("gameRoundId") Long gameRoundId) {
        return service.getGameState(gameRoundId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveGameState(GameState gameState) {
        service.saveGameState(gameState);
    }
}
