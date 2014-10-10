package vgs.gamestate.repository;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.ddl.KeyspaceDefinition;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.CqlQuery;
import com.netflix.astyanax.query.PreparedCqlQuery;
import com.netflix.astyanax.serializers.BytesArraySerializer;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import vgs.DataStore;
import vgs.Persistence;
import vgs.gamestate.entity.GameState;

@Stateless
@Persistence(DataStore.CASSANDRA)
public class CassandraGameStateRepository implements GameStateRepository {

    private AstyanaxContext<Keyspace> context;
    private Keyspace keyspace;
    private ColumnFamily<Integer, String> CF_GAME_STATE;

    @PostConstruct
    public void setContext()  {
        context = new AstyanaxContext.Builder()
                .forCluster("Test Cluster")
                .forKeyspace("test1")
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                                .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                                .setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
                )
                .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
                                .setPort(9160)
                                .setMaxConnsPerHost(20)
                                .setConnectTimeout(4000)
                                .setSocketTimeout(15000)
                                .setSeeds("127.0.0.1:9160")
                                .setLatencyAwareResetInterval(10000)
                )
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                        .setCqlVersion("3.0.0")
                        .setTargetCassandraVersion("2.0"))
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        keyspace = context.getClient();

        CF_GAME_STATE =
                new ColumnFamily<>("gamestate",
                        IntegerSerializer.get(),
                        StringSerializer.get());

        setupDB();
    }

    private void setupDB() {
        try {
            keyspace.createKeyspaceIfNotExists(ImmutableMap.<String, Object>builder()
                            .put("strategy_options", ImmutableMap.<String, Object>builder()
                                    .put("replication_factor", "2")
                                    .build())
                            .put("strategy_class", "SimpleStrategy")
                            .build()
            );
        /*keyspace.prepareQuery(CF_GAME_STATE)
                .withCql("DROP TABLE gamestate ")
                .execute();*/
            keyspace.prepareQuery(CF_GAME_STATE)
                    .withCql("CREATE TABLE IF NOT EXISTS gamestate (gameRoundId bigint PRIMARY KEY, occurrenceId bigint, userId bigint, gameId text, gameStateObject blob, gameRoundData blob)")
                    .execute();
        } catch (ConnectionException e) {
            throw new IllegalStateException(e);
        }
    }

    public void saveGameState(GameState gameState) {
        CqlQuery<Integer, String> query =
                keyspace.prepareQuery(CF_GAME_STATE)
                        .withCql("INSERT INTO gamestate (gameRoundId, occurrenceId, userId, gameId, gameStateObject, gameRoundData) VALUES (?,?,?,?,?,?)");

        PreparedCqlQuery<Integer, String> pStatement = query.asPreparedStatement();

        try {
            pStatement.withLongValue(gameState.getGameRoundId()).withLongValue(gameState.getOccurrenceId()).withLongValue(gameState.getUserId()).withStringValue(gameState.getGameId())
                    .withByteBufferValue(gameState.getGameStateObject(), BytesArraySerializer.get())
                    .withByteBufferValue(gameState.getGameRoundData(), BytesArraySerializer.get())
                    .execute();
        } catch (ConnectionException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<GameState> getGameState(Long gameRoundId) {
        Rows<Integer, String> rows = null;
        try {
            KeyspaceDefinition keyspaceDefinition = keyspace.describeKeyspace();
            rows = keyspace.prepareQuery(CF_GAME_STATE)
                    .withCql("SELECT * from gamestate where gameRoundId = ?")
                    .asPreparedStatement()
                    .withLongValue(gameRoundId)
                    .execute()
                    .getResult()
                    .getRows();
        } catch (ConnectionException e) {
            throw new IllegalStateException(e);
        }

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Row<Integer, String> row = rows.getRowByIndex(0);

        ColumnList<String> columns = row.getColumns();

        Long gameRoundId2 = columns.getColumnByIndex(0).getLongValue();
        String gameId = columns.getColumnByIndex(1).getStringValue();
        byte[] gameRoundData = columns.getColumnByIndex(2).getByteArrayValue();
        byte[] gameStateObject = columns.getColumnByIndex(3).getByteArrayValue();
        Long occurrenceId = columns.getColumnByIndex(4).getLongValue();
        Long userId = columns.getColumnByIndex(5).getLongValue();

        return Optional.of(new GameState(gameRoundId2, occurrenceId, userId, gameId, gameStateObject, gameRoundData));
    }
}
