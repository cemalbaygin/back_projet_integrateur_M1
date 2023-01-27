package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.GroupeMedicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeMedicamentsRepository extends JpaRepository<GroupeMedicament, Long> {
}