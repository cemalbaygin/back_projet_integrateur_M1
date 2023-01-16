package fr.uga.miage.m1.repository;
import fr.uga.miage.m1.model.Person;
import fr.uga.miage.m1.PersonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonsRepository extends JpaRepository<Person, PersonId> {
}