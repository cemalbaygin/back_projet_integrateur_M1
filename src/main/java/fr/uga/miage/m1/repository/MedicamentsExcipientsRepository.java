package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.GroupeMedicamentPrincipeActifEntity;
import fr.uga.miage.m1.entity.MedicamentExcipientEntity;
import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import fr.uga.miage.m1.model.MedicamentExcipientKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentsExcipientsRepository extends JpaRepository<MedicamentExcipientEntity, MedicamentExcipientKey> {
}