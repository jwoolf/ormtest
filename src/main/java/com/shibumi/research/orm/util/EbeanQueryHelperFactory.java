package com.shibumi.research.orm.util;

import com.avaje.ebean.EbeanServer;

public class EbeanQueryHelperFactory {

    private static final String ID = "id";

    public static <K,T> EbeanQueryHelper newEbeanQueryHelper(EbeanServer ebeanServer, Class<T> clazz, K keyType) {
        return new EbeanQueryHelperImpl<K,T>(ebeanServer, clazz);
    }

    public static <T> EbeanQueryHelper newEbeanQueryHelper(EbeanServer ebeanServer, Class<T> clazz) {
        return newEbeanQueryHelper(ebeanServer, clazz, Long.TYPE);
    }


    private static class EbeanQueryHelperImpl<K,T> implements EbeanQueryHelper<K,T> {

        private EbeanServer ebeanServer;
        private Class<T> clazz;

        EbeanQueryHelperImpl(EbeanServer ebeanServer, Class<T> clazz) {
            this.ebeanServer = ebeanServer;
            this.clazz = clazz;
        }

        public T findById(K key) {
            return ebeanServer.find(clazz)
                    .where()
                    .eq(ID, key)
                    .findUnique();
        }

    }

}
