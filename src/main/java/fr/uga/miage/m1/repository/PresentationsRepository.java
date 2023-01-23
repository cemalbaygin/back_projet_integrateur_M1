package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationsRepository extends JpaRepository<Presentation, Long> {

}