package com.atm.model.dao;


import java.util.List;

public interface Dao<Entity, Integer> {
    void create(Entity entity);

    Entity findById(Integer entityId);

    void update(Entity entity);

    void delete(Entity entity);

    List<Entity> findAll();
}
