package org.acardona.java.taller.Repositori;

import java.util.List;
import java.util.Optional;

public interface Repository <T>{
    T save (T entity);
    Optional <T> findById(String id);
    List <T> findAll();
    void delete(String id);
}
