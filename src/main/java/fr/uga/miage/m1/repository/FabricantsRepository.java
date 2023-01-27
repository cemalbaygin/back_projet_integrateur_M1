package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Fabricant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricantsRepository extends JpaRepository<Fabricant, Long> {
    Boolean existsByLibelle(String libelle); //Checks if there are any records by name

    Fabricant getByLibelle(String libelle);
}