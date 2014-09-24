package vgs;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import vgs.Schema;
import vgs.VGS;


public class CDITestConfig {


    @Produces
    @Alternative
    @RequestScoped
    @Schema(VGS.GameState)
    public EntityManager createEM() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {

        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("entityManagerTest");
        EntityManager em = emf.createEntityManager();

        return em;
    }
}