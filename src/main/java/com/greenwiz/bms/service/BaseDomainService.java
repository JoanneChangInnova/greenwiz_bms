package com.greenwiz.bms.service;

import java.util.Collection;
import java.util.List;

public interface BaseDomainService <P, E >{
    E save(E var1);

    E saveAndFlush(E var1);

    List<E> saveAll(Collection<E> var1);

    List<E> saveAllAndFlush(Collection<E> var1);

    void delete(E var1);

    void deleteAndFlush(E var1);

    void deleteAll(Collection<E> var1);

    void deleteAllAndFlush(Collection<E> var1);

    void deleteByPk(P var1);

    void deleteByPkAndFlush(P var1);

    E findByPk(P var1);
}
