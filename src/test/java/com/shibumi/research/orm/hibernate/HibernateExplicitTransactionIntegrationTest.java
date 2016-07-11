package com.shibumi.research.orm.hibernate;


import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.shibumi.research.orm.ebean.AnimalRepository;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import com.shibumi.research.orm.hibernate.model.FoodModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Properties;

import static org.junit.Assert.*;

public class HibernateExplicitTransactionIntegrationTest {

    private static EntityManager entityManager;

    private FoodRepository foodRepository = new FoodRepository(entityManager);

    @BeforeClass
    public static void setupEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa-hibernate");
        entityManager = factory.createEntityManager();
    }

    @AfterClass
    public static void takedownEntityManager() {
        entityManager.close();
    }


    @Test
    public void testFoodPersistence() throws Exception {
        FoodModel pizza = new FoodModel("pizza", "delicious", 4732);
        entityManager.getTransaction().begin();
        entityManager.persist(pizza);
        entityManager.getTransaction().commit();
        assertTrue(true);
    }

/*
    @Test
    public void testBasicEbeanTransactionRollback() throws Exception {

        AnimalModel zeus = new AnimalModel("cat", "neutered male", "Zeus");
        AnimalModel lucy = new AnimalModel("cat", "spayed female", "Lucy");

        ebeanServer.beginTransaction();
        try {
            animalRepository.create(zeus);
            animalRepository.create(lucy);
        } catch (Exception e) {
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        assertNotEquals(0, zeus.getId());
        assertNotEquals(0, lucy.getId());
        AnimalModel foundZeus = animalRepository.findAnimalById(zeus.getId());
        AnimalModel foundLucy = animalRepository.findAnimalById(lucy.getId());
        assertNull(foundZeus);
        assertNull(foundLucy);

    }

    @Test
    public void testBasicEbeanTransactionCommit() throws Exception {

        AnimalModel newton = new AnimalModel("cat", "neutered male", "Newton");
        AnimalModel maybelline = new AnimalModel("cat", "spayed female", "Maybelline");

        ebeanServer.beginTransaction();
        try {
            animalRepository.create(maybelline);
            animalRepository.create(newton);
            ebeanServer.commitTransaction();
        } catch (Exception e) {
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        assertNotEquals(0, newton.getId());
        assertNotEquals(0, maybelline.getId());
        AnimalModel foundNewton = animalRepository.findAnimalById(newton.getId());
        AnimalModel foundMaybelline = animalRepository.findAnimalById(maybelline.getId());
        assertNotNull(foundNewton);
        assertNotNull(foundMaybelline);

    }

*/
}
