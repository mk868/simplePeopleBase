package info.kucharczyk.java.simple_people_base.repository;

import java.util.Optional;

public interface Repository<K, T> {
    long count();

    void delete(T entity);

    void deleteAll();

    void deleteById(K id);

    boolean existsById(K id);

    Iterable<T> findAll();

    Optional<T> findById(K id);

    T save(T entity);
}