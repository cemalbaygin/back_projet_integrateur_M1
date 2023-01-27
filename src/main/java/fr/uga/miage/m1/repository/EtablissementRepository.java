package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Etablissement;
import fr.uga.miage.m1.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Integer> {
}
