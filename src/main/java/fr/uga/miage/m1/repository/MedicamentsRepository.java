package fr.uga.miage.m1.repository;
import fr.uga.miage.m1.entity.MedicamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentsRepository extends JpaRepository<MedicamentEntity, Long> {
}