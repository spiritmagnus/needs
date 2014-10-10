package vgs.gamestate.repository;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Singleton
@Persistence(DataStore.HAZELCAST)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HazelcastGameStateRepository implements GameStateRepository {

    private ClientConfig clientConfig;
    private HazelcastInstance client;

    @PostConstruct
    public void setup() {
        clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701");
        clientConfig.getGroupConfig().setName("test").setPassword("test_pass");
        client = HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Override
    public void saveGameState(GameState gameState) {
        IMap map = client.getMap("gameState");
        map.set(gameState.getGameRoundId(), gameState);
    }

    @Override
    public Optional<GameState> getGameState(Long gameRoundId) {
        IMap map = client.getMap("gameState");

        if (map == null) {
            return Optional.empty();
        }

        GameState gameState = (GameState) map.get(gameRoundId);
        return Optional.ofNullable(gameState);
    }
}
