package com.shibumi.research.orm.ebean;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.shibumi.research.orm.ebean.model.AnimalModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimalRepository.class);

    private EbeanServer ebeanServer;


    public AnimalModel create(AnimalModel animal) {
        ebeanServer.save(animal);
        return animal;
    }

    public AnimalModel findAnimalById(long id) {
        return ebeanServer.find(AnimalModel.class)
                .where()
                .eq("id", id)
                .findUnique();
    }

    public AnimalModel update(AnimalModel animal) {
        ebeanServer.update(animal);
        return animal;
    }

    public boolean delete(AnimalModel animal) {
        return ebeanServer.delete(animal);
    }



    public EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    public void setEbeanServer(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

}
