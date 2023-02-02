package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandesRepository extends JpaRepository<Commande, Integer> {

    @Query(value = "SELECT * FROM Commande c WHERE c.utilisateur_id = :user_id AND c.etat='panier' fetch first 1 row only", nativeQuery = true)
    Optional<Commande> getPanier(@Param("user_id") Integer utilisateurId);

    // on enleve une heure de plus car le serveur oracle est encore sur les heures d'été
    // 5 minute pour temps de paiement

    @Query(value = "SELECT * FROM Commande c WHERE c.etat='attente_paiement' AND c.date_achat < (CURRENT_TIMESTAMP + interval '1' hour - interval '5' minute)", nativeQuery = true)
    List<Commande> getCommandesEnAttente();
}
