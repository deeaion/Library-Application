package server.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.Objects;
@MappedSuperclass
public abstract class Identifiable<T extends Serializable> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    public Identifiable(T id) {
        this.id = id;
    }

    public Identifiable() {
    }


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
