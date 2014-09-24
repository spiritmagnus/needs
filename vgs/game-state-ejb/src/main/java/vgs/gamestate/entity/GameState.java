package vgs.gamestate.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GameState {

    @Id
    private final Long gameRoundId;
    private final Long occurrenceId;
    private final Long userId;
    private final String gameId;
    private final byte[] gameStateObject;
    private final byte[] gameRoundData;

    public GameState(Long gameRoundId, Long occurrenceId, Long userId, String gameId, byte[] gameStateObject, byte[] gameRoundData) {
        this.gameRoundId = gameRoundId;
        this.occurrenceId = occurrenceId;
        this.userId = userId;
        this.gameId = gameId;
        this.gameStateObject = gameStateObject;
        this.gameRoundData = gameRoundData;
    }

    protected GameState() {
        gameRoundId = null;
        occurrenceId = null;
        userId = null;
        gameId = null;
        gameStateObject = null;
        gameRoundData = null;
    }

    public Long getGameRoundId() {
        return gameRoundId;
    }

    public byte[] getGameStateObject() {
        return gameStateObject;
    }

    public byte[] getGameRoundData() {
        return gameRoundData;
    }

    public Long getOccurrenceId() {
        return occurrenceId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameState)) return false;

        GameState gameState = (GameState) o;

        return !(gameRoundId != null ? !gameRoundId.equals(gameState.gameRoundId) : gameState.gameRoundId != null);

    }

    @Override
    public int hashCode() {
        return gameRoundId != null ? gameRoundId.hashCode() : 0;
    }
}
