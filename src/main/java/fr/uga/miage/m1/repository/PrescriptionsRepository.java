package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionsRepository extends JpaRepository<Prescription, Long> {
    Boolean existsByLibelle(String libelle); //Checks if there are any records by name

    Prescription getByLibelle(String libelle);
}