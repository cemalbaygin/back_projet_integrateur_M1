package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import oracle.ucp.proxy.annotation.Pre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PresentationService {
    private final PresentationsRepository presentationRepo;
    private final AutoMapper autoMapper;
    private final PresentationMapper presentationMapper;

    private final EntityManager entityManager;


    public Page<PresentationMedicamentDTO> getPresentationsWithFilter(Optional<String> recherche, Pageable paging) {

        Specification<Presentation> specification = buildSpecifications(recherche);

        Page<Presentation> presentations = presentationRepo.findAll(specification,paging);

        return presentations.map(this.presentationMapper::presentationMedicamentDTO);
    }

    static Specification<Presentation> hasLibelle(String libelle) {
        return (present, cq, cb) -> cb.like(present.get("libelle"), "%"+libelle+"%");
    }

    static Specification<Presentation> hasMedicamentLibelle(String libelle) {
        return (present, cq, cb) -> {
            Join<Presentation, Medicament> medicament = present.join("medicament");
            return  cb.like(medicament.get("libelle"), "%"+  libelle +"%");
        };
    }

    static Specification<Presentation> hasPrincipeActifLibelle(String libelle) {
        return (present, cq, cb) -> {
            Join<Presentation, Medicament> medicament = present.join("medicament");
            Join<Medicament, GroupeMedicament> groupeMedicament = medicament.join("groupeMedicament");
            Join<GroupeMedicament, GroupeMedicamentPrincipeActif> groupeMedicamentPrincipeActif = groupeMedicament.join("groupeMedicamentAssoc");
            Join<GroupeMedicamentPrincipeActif, PrincipeActif> principeActif = groupeMedicamentPrincipeActif.join("principeActif");

            return  cb.like(principeActif.get("libelle"), "%"+libelle+"%");
        };
    }


    private Specification<Presentation> buildSpecifications(Optional<String> rechercheFilter){

        return (presentationRoot, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //filtre sur la recherche
            rechercheFilter.ifPresent(recherche -> {

                /*Join<Presentation, Medicament> medicament = presentationRoot.join("medicament");
                Join<Medicament, GroupeMedicament> groupeMedicament = medicament.join("groupeMedicament");
                Join<GroupeMedicament, GroupeMedicamentPrincipeActif> groupeMedicamentPrincipeActif = groupeMedicament.join("groupeMedicamentAssoc");
                Join<GroupeMedicamentPrincipeActif, PrincipeActif> principeActif = groupeMedicamentPrincipeActif.join("principeActif");
                predicates.add(criteriaBuilder.like(principeActif.get("libelle"), "%"+recherche+"%"));*/

                //Predicate predicate2 = criteriaBuilder.like(presentationRoot.get(Presentation_.LIBELLE), "%"+recherche+"%");

                Path<Object> principeActifPath =
                        presentationRoot.join(Presentation_.MEDICAMENT);

                Predicate predicate2 = criteriaBuilder.like(principeActifPath.get(Medicament_.LIBELLE), "%"+recherche+"%");
                        //.join(Medicament_.GROUPE_MEDICAMENT).join(GroupeMedicament_.GROUPE_MEDICAMENT_ASSOC)
                        //.join(GroupeMedicamentPrincipeActif_.PRINCIPE_ACTIF);

                //Predicate predicate2 = criteriaBuilder.like(principeActifPath.get(PrincipeActif_.LIBELLE), "%"+recherche+"%");

                predicates.add(predicate2);
            });
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
