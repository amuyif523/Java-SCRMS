package scrms.service;

import java.util.List;

/**
 * Generic CRUD interface exposing operations for the console UI.
 *
 * @param <T> entity type
 */
public interface CrudService<T> {

    /**
     * @return immutable snapshot of all entities
     */
    List<T> findAll();

    /**
     * Retrieves an entity by id or returns null.
     *
     * @param id identifier
     * @return entity or null
     */
    T findById(String id);

    /**
     * Persists a new entity.
     *
     * @param entity entity to create
     * @return stored entity
     */
    T create(T entity);

    /**
     * Updates an entity.
     *
     * @param entity entity with new values
     * @return updated entity
     */
    T update(T entity);

    /**
     * Removes an entity.
     *
     * @param id identifier to delete
     */
    void delete(String id);
}
