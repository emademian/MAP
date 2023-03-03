package com.example.labjavafx.repository;


import com.example.labjavafx.model.Entity;
import com.example.labjavafx.model.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<E, ID> {
    private final Map<ID, E> entities;

    private final Validator<E> validator;

    public InMemoryRepository(Validator<E> userValidator) {
        this.validator = userValidator;
        entities = new HashMap<>();
    }


    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new RepositoryException("ID cannot be null");
        }
        if (findOne(id) == null) {
            throw new RepositoryException("ID does not exist");
        }
        if (entities.get(id) != null) {
            E entity = entities.get(id);
            entities.remove(id);
            return entity;
        }
        return null;
    }

    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new RepositoryException("ID cannot be null");
        }
        return entities.get(id);
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

}
