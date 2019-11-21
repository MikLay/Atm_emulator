package com.atm.model.dao;


import java.util.List;

public interface Dao<Entity, T> {
    void create(Entity entity);

    Entity findById(T entityId);

    void update(Entity entity);

    void delete(Entity entity);

    List<Entity> findAll();
}
