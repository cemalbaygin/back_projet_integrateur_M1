package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Presentation_;
import jakarta.persistence.Query;
import oracle.ucp.proxy.annotation.Pre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationsRepository extends JpaRepository<Presentation, Long>, JpaSpecificationExecutor<Presentation> {

    //Page<Presentation> findAll(Specification<Presentation> specification, Pageable pageable);


}