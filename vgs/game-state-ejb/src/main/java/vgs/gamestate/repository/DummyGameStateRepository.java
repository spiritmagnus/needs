package vgs.gamestate.repository;

import java.util.Optional;

import javax.ejb.Stateless;

import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Stateless
@Persistence(DataStore.DUMMY)
public class DummyGameStateRepository implements GameStateRepository {

    @Override
    public void saveGameState(GameState gameState) {

    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {

        return Optional.ofNullable(new GameState(gameRoundId, 1L, 1L, "bananas", "asdf".getBytes(), "asdf".getBytes()));
    }
}
