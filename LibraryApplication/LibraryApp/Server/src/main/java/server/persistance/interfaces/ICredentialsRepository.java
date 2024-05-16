package server.persistance.interfaces;

import server.model.Credentials;

public interface ICredentialsRepository extends IRepository<Long, Credentials>{
    public Credentials findByUsername(String username);
    public Credentials findByEmail(String email);

}
