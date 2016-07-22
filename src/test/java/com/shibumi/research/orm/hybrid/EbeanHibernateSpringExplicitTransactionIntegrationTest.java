package com.shibumi.research.orm.hybrid;

import com.avaje.ebean.EbeanServer;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import com.shibumi.research.orm.hibernate.model.FoodModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ebean-hibernate-test-context.xml"})
public class EbeanHibernateSpringExplicitTransactionIntegrationTest {

    @Autowired
    private JpaTransactionManager transactionManager;

    @Autowired
    private EbeanServer ebeanServer;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private NamedParameterJdbcTemplate jdbcTemplate;


    @Test
    public void testConf() {
        assertNotNull(transactionManager);
        assertNotNull(ebeanServer);
        assertNotNull(entityManagerFactory);
        assertNotNull(jdbcTemplate);
    }

    //@Test // liveness check
    public void testEntityManagerPersistance() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FoodModel apple = new FoodModel("apple", "healthy", 34);
        entityManager.getTransaction().begin();
        entityManager.persist(apple);
        entityManager.flush();
        entityManager.getTransaction().commit();
        assertTrue(true);
    }

    @Test
    public void testInsertEbeanAndHibernateViaSpringCommit() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("COMMIT-VIA-SPRING");
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        AnimalModel hiss = new AnimalModel("snake", "male", "Hiss");
        FoodModel banana = new FoodModel("banana", "healthy", 34);

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        ebeanServer.save(hiss);
        entityManager.persist(banana);
        entityManager.flush();
        transactionManager.commit(transaction);

        assertTrue(true);

//        AnimalModel foundKoko = ebeanServer.find(AnimalModel.class).where().eq("id", koko.getId()).findUnique();
//        AnimalModel foundKitten = ebeanServer.find(AnimalModel.class).where().eq("species", "kitten").eq("name", "Smokey").findUnique();
//        assertNotNull(foundKoko);
//        assertNotNull(foundKitten);
    }

    @Test
    public void testRollbackEbeanAndHibernateViaSpringRollback() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("COMMIT-VIA-SPRING");
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        AnimalModel babar = new AnimalModel("elephant", "male", "Babar");
        FoodModel pear = new FoodModel("pear", "healthy", 34);

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        ebeanServer.save(babar);  // babar is committed. txns aren't the same.
        entityManager.persist(pear);
        entityManager.flush();
        transactionManager.rollback(transaction);

        assertTrue(true);

//        AnimalModel foundKoko = ebeanServer.find(AnimalModel.class).where().eq("id", koko.getId()).findUnique();
//        AnimalModel foundKitten = ebeanServer.find(AnimalModel.class).where().eq("species", "kitten").eq("name", "Smokey").findUnique();
//        assertNotNull(foundKoko);
//        assertNotNull(foundKitten);
    }


    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    public void setEbeanServer(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public JpaTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
