package vgs.gamestate.rest.gamestate;

import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.Test;
import org.junit.runner.RunWith;

import vgs.CDITestConfig;
import vgs.gamestate.entity.GameState;
import vgs.gamestate.service.GameStateService;

@RunWith(CdiRunner.class)
@ActivatedAlternatives(CDITestConfig.class)
public class GameStateControllerTest {

    @Inject
    ContextController contextController;
    @Inject
    private GameStateService service;


    @Test
    public void shouldDoStuff() throws SystemException, NotSupportedException {

        long before = System.currentTimeMillis();

        IntStream.range(0, 1000).parallel().forEach(i -> {
                    service.saveGameState(new GameState((long) i, 1L, 1L, "bananas", "test".getBytes(), "test2".getBytes()));
                }
        );

        //IntStream.range(0, 1000).parallel().forEach(i -> controller.getGameState((long) i));
        System.out.println("Time: " + (System.currentTimeMillis() - before));
    }
}