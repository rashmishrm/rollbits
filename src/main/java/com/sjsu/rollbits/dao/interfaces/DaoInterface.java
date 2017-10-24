package com.sjsu.rollbits.dao.interfaces;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dhrumil on 10/23/17.
 */
public interface DaoInterface<T, Id extends Serializable> {
    public void persist(T entity);
    public void update(T entity);
    public T findById(Id id);
    public void delete(T entity);
    public List<T> findAll();
    public void deleteAll();
}
