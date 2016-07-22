package com.shibumi.research.orm.hybrid;


import com.avaje.ebean.EbeanServer;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ebean-jdbc-test-context.xml"})
public class EbeanJDBCExplicitTransactionIntegrationTest {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private EbeanServer ebeanServer;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    public void testConf() {
        assertNotNull(transactionManager);
        assertNotNull(ebeanServer);
        assertNotNull(jdbcTemplate);
    }

    @Test // NOTE - THIS TEST DOESN'T MEAN THAT THE COMMITS ARE BOUND TO THE SAME TXN. SEE TEST BELOW.
    public void testInsertEbeanAndJdbcViaEbeanCommit() {
        AnimalModel leo = new AnimalModel("lion", "male", "Leo");
        ebeanServer.beginTransaction();
        try {
            ebeanServer.save(leo);
            jdbcTemplate.getJdbcOperations().update("insert into animal (species, gender, name) values (?,?,?)", "wolf", "male", "John");
            ebeanServer.commitTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        AnimalModel foundLion = ebeanServer.find(AnimalModel.class).where().eq("id", leo.getId()).findUnique();
        AnimalModel foundWolf = ebeanServer.find(AnimalModel.class).where().eq("species", "wolf").eq("name", "John").findUnique();
        assertNotNull(foundLion);
        assertNotNull(foundWolf);
    }


    @Test //NOTE - THIS TEST FAILS BECAUSE THE JDBC TEMPLATE TXN ISN'T SYNC'D WITH THE EBEAN TXN
    public void testRollbackEbeanAndJdbcViaEbeanRollback() {
        AnimalModel babar = new AnimalModel("elephant", "male", "Babar");
        ebeanServer.beginTransaction();
        try {
            ebeanServer.save(babar);
            jdbcTemplate.getJdbcOperations().update("insert into animal (species, gender, name) values (?,?,?)", "monkey", "male", "George");
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }

        AnimalModel foundBabar = ebeanServer.find(AnimalModel.class).where().eq("id", babar.getId()).findUnique();
        AnimalModel foundCurious = ebeanServer.find(AnimalModel.class).where().eq("species", "monkey").eq("name", "George").findUnique();
        assertNull(foundBabar);
        assertNull(foundCurious);
    }



    @Test
    public void testInsertEbeanAndJdbcViaSpringCommit() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("COMMIT-VIA-SPRING");
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        AnimalModel koko = new AnimalModel("gorilla", "female", "Koko");

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            jdbcTemplate.getJdbcOperations().update("insert into animal (species, gender, name) values (?,?,?)", "kitten", "female", "Smokey");
            ebeanServer.save(koko);
            transactionManager.commit(transaction);
        } finally {
        }

        AnimalModel foundKoko = ebeanServer.find(AnimalModel.class).where().eq("id", koko.getId()).findUnique();
        AnimalModel foundKitten = ebeanServer.find(AnimalModel.class).where().eq("species", "kitten").eq("name", "Smokey").findUnique();
        assertNotNull(foundKoko);
        assertNotNull(foundKitten);
    }

    @Test
    public void testRollbackEbeanAndJdbcViaSpringRollback() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("ROLLBACK-VIA-SPRING");
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        AnimalModel frog = new AnimalModel("frog", "male", "Frog");

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            jdbcTemplate.getJdbcOperations().update("insert into animal (species, gender, name) values (?,?,?)", "toad", "male", "Toad");
            ebeanServer.save(frog);
            transactionManager.rollback(transaction);
        } finally {
        }

        AnimalModel foundFrog = ebeanServer.find(AnimalModel.class).where().eq("id", frog.getId()).findUnique();
        AnimalModel foundToad = ebeanServer.find(AnimalModel.class).where().eq("species", "toad").eq("name", "Toad").findUnique();
        assertNull(foundFrog);
        assertNull(foundToad);
    }


    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    public void setEbeanServer(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
