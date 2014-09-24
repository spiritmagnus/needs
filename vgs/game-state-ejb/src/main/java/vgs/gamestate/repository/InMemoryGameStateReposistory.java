package vgs.gamestate.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Persistence(DataStore.IN_MEMORY)
public class InMemoryGameStateReposistory implements GameStateRepository {

    private final ConcurrentMap<Long, GameState> gameStates = new ConcurrentHashMap<>();

    @Override
    public void saveGameState(GameState gameState) {
        gameStates.put(gameState.getGameRoundId(), gameState);
    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {
        return Optional.ofNullable(gameStates.get(gameRoundId));
    }
}
