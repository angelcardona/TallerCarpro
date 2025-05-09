package org.acardona.java.taller.Repositori;

import org.acardona.java.taller.domain.Supplier;

import java.util.List;
import java.util.Optional;

public interface Repository <T>{
    T save (T entity);

    Supplier save(Supplier supplier);

    Optional <T> findById(String id);
    List <T> findAll();
    void delete(String id);
}
