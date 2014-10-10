package vgs.gamestate.service;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;

import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;
import vgs.gamestate.repository.GameStateRepository;

@Stateless
public class GameStateService {

    @Inject
    @Persistence(DataStore.CASSANDRA)
    private GameStateRepository repository;

    public GameState getGameState(Long gameRoundId) {
        return repository.getGameState(gameRoundId).orElseThrow(new Supplier<RuntimeException>() {
            @Override
            public RuntimeException get() {
                return new IllegalArgumentException("GameState not found: " + gameRoundId);
            }
        });
    }


    public void saveGameState(GameState gameState) {
        repository.saveGameState(gameState);
    }
}
