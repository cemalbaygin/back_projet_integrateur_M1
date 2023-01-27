package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Excipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcipientsRepository extends JpaRepository<Excipient, Long> {
}