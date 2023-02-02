package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Presentation;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresentationsRepository extends JpaRepository<Presentation, Long>, JpaSpecificationExecutor<Presentation> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value ="10000")})
    Optional<Presentation> findByCodeCIP13(Long codeCIP13);
}