package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.PresentationCommandeType;
import fr.uga.miage.m1.model.key.CommandePresentationKey;
import fr.uga.miage.m1.model.key.CommandeTypePresentationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationsCommandesTypeRepository extends JpaRepository<PresentationCommandeType, CommandeTypePresentationKey> {


}
