package com.shibumi.research.orm.util;

public interface EbeanQueryHelper<K, T> {

    T findById(K key);

}
