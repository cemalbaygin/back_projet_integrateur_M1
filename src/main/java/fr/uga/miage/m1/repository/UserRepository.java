package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Utilisateur, Integer> {

  Optional<Utilisateur> findByEmail(String email);

}
