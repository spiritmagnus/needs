package vgs.gamestate.repository;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.ejb.Stateless;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import com.netflix.astyanax.serializers.JacksonSerializer;
import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Stateless
@Persistence(DataStore.REDIS)
public class RiakGameStateRepository implements GameStateRepository {

    private final int port = 6379;

    private RiakClient client;

    private final JacksonSerializer<GameState> serializer = new JacksonSerializer<>(GameState.class);
    private final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public RiakGameStateRepository() {
        try {
            client = RiakClient.newClient("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveGameState(GameState gameState) {

        Namespace ns = new Namespace("default", "my_bucket");
        Location location = new Location(ns, gameState.getGameRoundId().toString());
        RiakObject riakObject = new RiakObject();
        riakObject.setValue(BinaryValue.create(serializer.toByteBuffer(gameState).array()));
        StoreValue store = new StoreValue.Builder(riakObject)
                .withLocation(location)
                .withOption(StoreValue.Option.W, new Quorum(3)).build();

        try {
            client.execute(store);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {
        Namespace ns = new Namespace("default", "my_bucket");
        Location location = new Location(ns, gameRoundId.toString());
        FetchValue fv = new FetchValue.Builder(location).build();
        FetchValue.Response response = null;
        try {
            response = client.execute(fv);
            RiakObject obj = response.getValue(RiakObject.class);
            GameState gameState = serializer.fromByteBuffer(ByteBuffer.wrap(obj.getValue().getValue()));

            return Optional.ofNullable(gameState);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
