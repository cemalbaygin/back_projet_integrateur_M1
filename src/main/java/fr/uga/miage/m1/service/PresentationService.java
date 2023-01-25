package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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


    public Normalizer getPresentationsWithFilter(Optional<String> recherche, Pageable paging) {
        Page<Presentation> presentations;

        if (recherche.isPresent()) {
            Specification<Presentation> specification = hasPrincipeActifLibelle(recherche.get());
           /* specification = specification.or(hasMedicamentLibelle(recherche.get()));
            specification = specification.or(hasPrincipeActifLibelle(recherche.get()));*/

            presentations = presentationRepo.findAll(specification, paging);
        } else {
            presentations = presentationRepo.findAll(paging);
        }

        List<Presentation> res = presentations.getContent();

        List<PresentationMedicamentDTO> collect = res.stream().map(e -> presentationMapper.presentationMedicamentDTO(e)).collect(Collectors.toList());

        return new Normalizer(collect, presentations);


    }

    static Specification<Presentation> hasLibelle(String libelle) {
        return (present, cq, cb) -> cb.like(present.get("libelle"), "%" + libelle + "%");
    }

    static Specification<Presentation> hasMedicamentLibelle(String libelle) {
        return (present, cq, cb) -> {
            Join<Presentation, Medicament> medicament = present.join("medicament");
            return cb.like(medicament.get("libelle"), "%" + libelle + "%");
        };
    }

    static Specification<Presentation> hasPrincipeActifLibelle(String libelle) {
        return (present, cq, cb) -> {
            Join<Presentation, Medicament> medicament = present.join("medicament");
            Join<Medicament, GroupeMedicament> groupeMedicament = medicament.join("groupeMedicament");
            Join<GroupeMedicament, GroupeMedicamentPrincipeActif> groupeMedicamentPrincipeActif = groupeMedicament.join("groupeMedicamentAssoc");
            Join<GroupeMedicamentPrincipeActif, PrincipeActif> principeActif = groupeMedicamentPrincipeActif.join("principeActif");

            return cb.like(principeActif.get("libelle"), "%PROSTATE%");
        };
    }


    private Specification<Presentation> buildSpecifications(Optional<String> rechercheFilter) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //filtre sur la recherche
            rechercheFilter.ifPresent(recherche -> {

                Predicate predicate1 = criteriaBuilder.like(root.get(Presentation_.LIBELLE), "%" + recherche + "%");

                Path<Object> principeActifPath =
                        root.join(Presentation_.MEDICAMENT)
                                .get(Medicament_.GROUPE_MEDICAMENT).get(GroupeMedicament_.GROUPE_MEDICAMENT_ASSOC)
                                .get(GroupeMedicamentPrincipeActif_.PRINCIPE_ACTIF);

                Predicate predicate2 = criteriaBuilder.like(principeActifPath.get(PrincipeActif_.LIBELLE), "%" + recherche + "%");

                predicates.add(criteriaBuilder.or(predicate1));
            });
            return predicates.get(0);
        };
    }
}
