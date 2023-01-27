package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Medicament;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MedicamentsRepository extends JpaRepository<Medicament, Long> {
    List<Medicament> findByGroupeMedicamentIsNull();

    @Modifying
    @Transactional
    @Query("update Medicament m set m.estReference=true where m.groupeMedicament is null")
    void putNullGroupeMedicamentToReference();
}