package vgs.gamestate.repository;

import java.nio.ByteBuffer;
import java.util.Optional;

import javax.ejb.Stateless;

import com.netflix.astyanax.serializers.JacksonSerializer;
import redis.clients.jedis.Jedis;

import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Stateless
@Persistence(DataStore.REDIS)
public class RedisGameStateRepository implements GameStateRepository {

    private final int port = 6379;

    private final Jedis jedis = new Jedis("localhost", port, 5000);
    private final JacksonSerializer<GameState> serializer = new JacksonSerializer<>(GameState.class);
    private final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    @Override
    public void saveGameState(GameState gameState) {

        buffer.clear();
        buffer.putLong(gameState.getGameRoundId());
        jedis.set(buffer.array(), serializer.toByteBuffer(gameState).array());
    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {
        buffer.clear();
        buffer.putLong(gameRoundId);

        byte[] bytes = jedis.get(buffer.array());

        if (bytes == null) {
            return Optional.empty();
        }

        GameState gameState = serializer.fromByteBuffer(ByteBuffer.wrap(bytes));
        return Optional.ofNullable(gameState);
    }
}
