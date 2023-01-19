package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.MedicamentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MedicamentsRepository extends JpaRepository<MedicamentEntity, Long> {
    List<MedicamentEntity> findByGroupeMedicamentIsNull();

    @Modifying
    @Transactional
    @Query("update MedicamentEntity m set m.estReference=true where m.groupeMedicament is null")
    void putNullGroupeMedicamentToReference();
}