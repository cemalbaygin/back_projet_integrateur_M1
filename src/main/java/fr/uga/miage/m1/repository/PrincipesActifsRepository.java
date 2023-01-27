package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.PrincipeActif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipesActifsRepository extends JpaRepository<PrincipeActif, Long> {
}