package org.acardona.java.taller.Repositori;

import org.acardona.java.taller.domain.Repair;
import org.acardona.java.taller.domain.Supplier;
import org.acardona.java.taller.domain.WeeklyBalance;

import java.util.List;
import java.util.Optional;

public interface Repository <T>{
    T save (T entity);

    WeeklyBalance save(WeeklyBalance weeklyBalance);

    Optional <T> findById(String id);
    List <T> findAll();
    void delete(String id);
}
