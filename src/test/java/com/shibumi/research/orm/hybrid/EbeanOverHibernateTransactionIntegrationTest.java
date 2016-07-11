package com.shibumi.research.orm.hybrid;


import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.shibumi.research.orm.ebean.AnimalRepository;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import com.shibumi.research.orm.hibernate.FoodRepository;
import com.shibumi.research.orm.hibernate.model.FoodModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class EbeanOverHibernateTransactionIntegrationTest {


    private static EbeanServer ebeanServer;
    private static EntityManager entityManager;

    private AnimalRepository animalRepository = new AnimalRepository();
    private FoodRepository foodRepository = new FoodRepository(entityManager);

    @BeforeClass
    public static void setupORM() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("mysql");
        //serverConfig.setUseJtaTransactionManager(true);
        serverConfig.loadFromProperties();
        // serverConfig.setDdlGenerate( true );
        // serverConfig.setDdlRun( true );
        serverConfig.addClass(AnimalModel.class);
        serverConfig.addClass(com.shibumi.research.orm.ebean.model.AnimalModel.class); // classname explicit for clarity
        serverConfig.setDefaultServer(true);
        ebeanServer = EbeanServerFactory.create( serverConfig );

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa-hibernate");
        entityManager = factory.createEntityManager();

    }

    @AfterClass
    public static void takedownORM() {
        entityManager.close();
        ebeanServer.shutdown( false, false );
    }


//    @Test
//    public void testEbeanTransactionAndHibernateEntityCommit() throws Exception {
//
//        AnimalModel bessy = new AnimalModel("cow", "female", "Bessy");
//        FoodModel sushi = new FoodModel("sushi", "delicious", 95);
//
//        ebeanServer.beginTransaction();
//        entityManager.joinTransaction();
//        try {
//            animalRepository.create(bessy);
//            foodRepository.create(sushi);
//            ebeanServer.commitTransaction();
//        } catch (Exception e) {
//            ebeanServer.rollbackTransaction();
//        } finally {
//            ebeanServer.endTransaction();
//        }
//
//        assertNotEquals(0, bessy.getId());
//        assertNotEquals(0, sushi.getId());
//        AnimalModel foundBessy = animalRepository.findAnimalById(bessy.getId());
//        FoodModel foundSushi = foodRepository.findAnimalById(sushi.getId());
//        assertNotNull(foundBessy);
//        assertNotNull(foundSushi);
//
//    }

    @Test
    public void testEbeanTransactionAndHibernateEntityCommit() throws Exception {

        AnimalModel kermit = new AnimalModel("frog", "male", "Kermit");
        FoodModel sushi = new FoodModel("sushi", "delicious", 95);

        ebeanServer.beginTransaction();
        entityManager.getTransaction();
        try {
            animalRepository.create(kermit);
            foodRepository.create(sushi);
            ebeanServer.commitTransaction();
        } catch (Exception e) {
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        assertNotEquals(0, kermit.getId());
        assertNotEquals(0, sushi.getId());
        AnimalModel foundBessy = animalRepository.findAnimalById(kermit.getId());
        FoodModel foundSushi = foodRepository.findAnimalById(sushi.getId());
        assertNotNull(foundBessy);
        assertNotNull(foundSushi);

    }

}
