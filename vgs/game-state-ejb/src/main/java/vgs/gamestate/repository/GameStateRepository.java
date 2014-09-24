package vgs.gamestate.repository;

import java.util.Optional;

import vgs.gamestate.entity.GameState;

public interface GameStateRepository {
    void saveGameState(GameState gameState);

    Optional<GameState> getGameState(Long gameRoundId);
}
