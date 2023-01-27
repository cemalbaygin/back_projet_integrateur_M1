package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.TypeAdministration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesAdministrationRepository extends JpaRepository<TypeAdministration, Long> {
    Boolean existsByLibelle(String libelle);

    TypeAdministration getByLibelle(String libelle);
}