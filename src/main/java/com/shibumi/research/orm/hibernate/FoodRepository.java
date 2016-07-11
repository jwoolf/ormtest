package com.shibumi.research.orm.hibernate;


import com.shibumi.research.orm.hibernate.model.FoodModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class FoodRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodRepository.class);

    private EntityManager entityManager;

    public FoodRepository(EntityManager em) {
        entityManager = em;
    }

    public FoodModel create(FoodModel food) {
        entityManager.persist(food);
        return food;
    }

    public FoodModel findAnimalById(long id) {
        return entityManager.find(FoodModel.class, id);
    }

    public FoodModel update(FoodModel food) {
        throw new RuntimeException("not implemented!");
    }

    public void delete(FoodModel food) {
        entityManager.remove(food);
    }



}
