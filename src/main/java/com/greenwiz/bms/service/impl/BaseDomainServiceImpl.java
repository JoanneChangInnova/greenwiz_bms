package com.greenwiz.bms.service.impl;

import com.greenwiz.bms.service.BaseDomainService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public abstract class BaseDomainServiceImpl<P, E> implements BaseDomainService<P, E> {

    @Getter(AccessLevel.PROTECTED)
    private EntityManager entityManager;

    public BaseDomainServiceImpl() {
    }

    protected abstract JpaRepository<E, P> getJpaRepository();

    @Transactional(Transactional.TxType.REQUIRED)
    public E save(E e) {
        return this.getJpaRepository().save(e);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<E> saveAll(Collection<E> collection) {
        return this.getJpaRepository().saveAll(collection);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(E e) {
        this.getJpaRepository().delete(e);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAll(Collection<E> collection) {
        this.getJpaRepository().deleteAll(collection);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteByPk(P p) {
        this.getJpaRepository().deleteById(p);
    }

    public E findByPk(P p) {
        return this.getJpaRepository().findById(p).orElse(null);
    }

    protected void flush() {
        this.getJpaRepository().flush();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public E saveAndFlush(E e) {
        return this.getJpaRepository().saveAndFlush(e);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<E> saveAllAndFlush(Collection<E> collection) {
        return this.getJpaRepository().saveAllAndFlush(collection);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAndFlush(E e) {
        this.delete(e);
        this.flush();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAllAndFlush(Collection<E> collection) {
        this.deleteAll(collection);
        this.flush();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteByPkAndFlush(P p) {
        this.deleteByPk(p);
        this.flush();
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
