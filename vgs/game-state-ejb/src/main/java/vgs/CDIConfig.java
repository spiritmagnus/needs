package vgs;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CDIConfig {

    @Produces
    @Schema(VGS.GameState)
    @PersistenceContext(unitName = "gameState")
    private EntityManager em;
}
