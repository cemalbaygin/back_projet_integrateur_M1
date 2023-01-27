package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.GroupeMedicamentPrincipeActif;
import fr.uga.miage.m1.model.key.GroupeMedicamentPrincipeActifKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeMedicamentsPrincipeActifRepository extends JpaRepository<GroupeMedicamentPrincipeActif, GroupeMedicamentPrincipeActifKey> {
}