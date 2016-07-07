package com.shibumi.research.orm.ebean;


import com.avaje.ebean.Ebean;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimalRepository.class);


    public AnimalModel create(AnimalModel animal) {
        Ebean.save(animal);
        return animal;
    }

    public AnimalModel findAnimalById(long id) {
        return Ebean.find(AnimalModel.class)
                .where()
                .eq("id", id)
                .findUnique();
    }

    public AnimalModel update(AnimalModel animal) {
        Ebean.update(animal);
        return animal;
    }

    public boolean delete(AnimalModel animal) {
        return Ebean.delete(animal);
    }


}
