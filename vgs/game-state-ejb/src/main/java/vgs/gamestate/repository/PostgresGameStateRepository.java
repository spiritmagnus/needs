package vgs.gamestate.repository;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import vgs.DataStore;
import vgs.Schema;
import vgs.Persistence;
import vgs.VGS;
import vgs.gamestate.entity.GameState;

@Stateless
@Persistence(DataStore.DB)
public class PostgresGameStateRepository implements GameStateRepository {

    @Inject
    @Schema(VGS.GameState)
    private EntityManager em;

    @Override
    public void saveGameState(GameState gameState) {
        em.persist(gameState);
    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {

        GameState gameState = em.find(GameState.class, gameRoundId);
        return Optional.ofNullable(gameState);
    }
}
