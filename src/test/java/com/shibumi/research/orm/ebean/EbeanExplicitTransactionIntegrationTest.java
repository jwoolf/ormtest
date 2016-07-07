package com.shibumi.research.orm.ebean;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EbeanExplicitTransactionIntegrationTest {

    private static EbeanServer ebeanServer;

    private AnimalRepository animalRepository = new AnimalRepository();

    @BeforeClass
    public static void setupEbeanServer() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("mysql");
        serverConfig.loadFromProperties();
        // serverConfig.setDdlGenerate( true );
        // serverConfig.setDdlRun( true );
        serverConfig.addClass(AnimalModel.class);
        serverConfig.addClass(com.shibumi.research.orm.ebean.model.AnimalModel.class); // classname explicit for clarity
        serverConfig.setDefaultServer(true);

        ebeanServer = EbeanServerFactory.create( serverConfig );

    }

    @AfterClass
    public static void takedownEbeanServer() {
        ebeanServer.shutdown( false, false );
    }


    @Test
    public void testAnimalPersistence() throws Exception {
        AnimalModel geronimo = new AnimalModel("cat", "neutered male", "Geronimo");
        ebeanServer.save(geronimo);
        assertTrue(true);
    }


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


}
