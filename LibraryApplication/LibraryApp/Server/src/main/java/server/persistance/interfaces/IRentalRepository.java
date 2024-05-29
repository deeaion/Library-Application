package server.persistance.interfaces;

import server.model.Rental;

public interface IRentalRepository extends IRepository<Long, Rental>{
    public Iterable<Rental> findRentalsByUser(Long userId);

    void flush();
}
