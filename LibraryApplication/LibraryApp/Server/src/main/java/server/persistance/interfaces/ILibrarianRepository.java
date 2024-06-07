package server.persistance.interfaces;

import server.model.Librarian;

public interface ILibrarianRepository extends IRepository<Long, Librarian> {
    public Librarian findByUniqueCode(String uniqueCode);
    public Librarian findByUserId(Long userId);
    public Librarian findByUsername(String username);
}
