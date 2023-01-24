package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.model.compositeKey.CommandePresentationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandesPresentationRepository extends JpaRepository<CommandePresentation, CommandePresentationKey> {


}
