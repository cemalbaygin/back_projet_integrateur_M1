package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.GroupeMedicamentEntity;
import fr.uga.miage.m1.entity.GroupeMedicamentPrincipeActifEntity;
import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeMedicamentsPrincipeActifRepository extends JpaRepository<GroupeMedicamentPrincipeActifEntity, GroupeMedicamentPrincipeActifKey> {
}