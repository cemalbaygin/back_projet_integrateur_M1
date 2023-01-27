package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.MedicamentExcipient;
import fr.uga.miage.m1.model.key.MedicamentExcipientKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentsExcipientsRepository extends JpaRepository<MedicamentExcipient, MedicamentExcipientKey> {
}