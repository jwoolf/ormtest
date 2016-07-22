package com.shibumi.research.orm.ebean;


import com.avaje.ebean.EbeanServer;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import com.shibumi.research.orm.util.EbeanQueryHelper;
import com.shibumi.research.orm.util.EbeanQueryHelperFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ebean-jdbc-test-context.xml"})
public class EbeanExplicitTransactionIntegrationTest {

    @Autowired
    private EbeanServer ebeanServer;



    @Test
    public void testConf() throws Exception {
        assertNotNull(ebeanServer);
    }


    @Test
    public void testAnimalPersistence() throws Exception {
        AnimalModel geronimo = new AnimalModel("cat", "neutered male", "Geronimo");
        ebeanServer.save(geronimo);
        assertNotEquals(0, geronimo.getId());
    }


    @Test
    public void testBasicEbeanTransactionRollback() throws Exception {

        EbeanQueryHelper<Long, AnimalModel> helper = EbeanQueryHelperFactory.newEbeanQueryHelper(ebeanServer, AnimalModel.class);

        AnimalModel zeus = new AnimalModel("cat", "neutered male", "Zeus");
        AnimalModel lucy = new AnimalModel("cat", "spayed female", "Lucy");

        ebeanServer.beginTransaction();
        try {
            ebeanServer.save(zeus);
            ebeanServer.save(lucy);
        } catch (Exception e) {
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        assertNotEquals(0, zeus.getId());
        assertNotEquals(0, lucy.getId());
        AnimalModel foundZeus = helper.findById(zeus.getId());
        AnimalModel foundLucy = helper.findById(lucy.getId());
        assertNull(foundZeus);
        assertNull(foundLucy);

    }

//    @Test
//    public void testBasicEbeanTransactionCommit() throws Exception {
//
//        AnimalModel newton = new AnimalModel("cat", "neutered male", "Newton");
//        AnimalModel maybelline = new AnimalModel("cat", "spayed female", "Maybelline");
//
//        ebeanServer.beginTransaction();
//        try {
//            animalRepository.create(maybelline);
//            animalRepository.create(newton);
//            ebeanServer.commitTransaction();
//        } catch (Exception e) {
//            ebeanServer.rollbackTransaction();
//        } finally {
//            ebeanServer.endTransaction();
//        }
//
//        assertNotEquals(0, newton.getId());
//        assertNotEquals(0, maybelline.getId());
//        AnimalModel foundNewton = animalRepository.findAnimalById(newton.getId());
//        AnimalModel foundMaybelline = animalRepository.findAnimalById(maybelline.getId());
//        assertNotNull(foundNewton);
//        assertNotNull(foundMaybelline);
//
//    }




    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    public void setEbeanServer(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

}
