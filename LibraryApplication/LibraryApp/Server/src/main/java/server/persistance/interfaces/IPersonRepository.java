package server.persistance.interfaces;

import server.model.Person;

public interface IPersonRepository extends IRepository<Long, Person> {
    public Person findByCPN(String cnp);
    public Iterable<Person> findByName(String name);


}
