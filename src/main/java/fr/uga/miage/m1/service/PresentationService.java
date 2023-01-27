package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PresentationService {
    private final PresentationsRepository presentationRepo;
    private final PresentationMapper presentationMapper;



    public Page<PresentationMedicamentDTO> getPresentationsWithFilter(Optional<String> recherche, Pageable paging) {

        Specification<Presentation> specification = buildSpecifications(recherche);

        Page<Presentation> presentations = presentationRepo.findAll(specification,paging);

        return presentations.map(this.presentationMapper::presentationMedicamentDTO);
    }

    public PresentationCompleteDTO getPresentation(Long codeCIP13) throws NoSuchElementException{
        return presentationMapper.entityToDto(presentationRepo.findById(Long.valueOf(codeCIP13))
                .orElseThrow(() -> new NoSuchElementException("Presentation not found with codeCIP13 : "+codeCIP13)));
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
