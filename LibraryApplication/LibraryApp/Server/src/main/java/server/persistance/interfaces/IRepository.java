package server.persistance.interfaces;

import jakarta.persistence.Entity;
import server.model.Identifiable;

import java.io.Serializable;

public interface IRepository<ID extends Serializable,E extends Identifiable<ID>> {
    public E add(E obj);
    public E remove(ID id);
    public E update(E obj);
    public E get(ID id);
    public Iterable<E> getAll();
}
