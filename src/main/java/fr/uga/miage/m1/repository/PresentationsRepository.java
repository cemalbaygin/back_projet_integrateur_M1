package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationsRepository extends PagingAndSortingRepository<Presentation, Long>, JpaRepository<Presentation, Long> {

}