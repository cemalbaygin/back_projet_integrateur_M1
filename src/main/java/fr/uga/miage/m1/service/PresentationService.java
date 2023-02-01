package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.model.request.RequestRecheche;
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



    public Page<PresentationMedicamentDTO> getPresentationsWithFilter(RequestRecheche recherche,
                                                                      Pageable paging) {

        Specification<Presentation> specification = buildSpecifications(
                recherche.nomMedicament,
                recherche.principeActif,
                recherche.estReference,
                recherche.estEnStock,
                recherche.fabricants);

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


    private Specification<Presentation> buildSpecifications(Optional<String> nomMedicamentFilter,
                                                            Optional<String> principeActifFilter,
                                                            Optional<Boolean> estReferenceFilter,
                                                            Optional<Boolean> estEnStockFilter,
                                                            Optional<List<String>> fabricantsFilter){

        return (presentationRoot, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //filtre sur la recherche
            nomMedicamentFilter.ifPresent(nomMedicament -> {


                Join<Medicament, Presentation> medicament = presentationRoot.join("medicament");
                predicates.add(criteriaBuilder.like(medicament.get(Medicament_.LIBELLE), "%"+nomMedicament+"%"));

                /*Path<Object> principeActifPath =
                        presentationRoot.join(Presentation_.MEDICAMENT);

                Predicate predicate1 = criteriaBuilder.like(principeActifPath.get(Medicament_.LIBELLE), "%"+recherche+"%");*/
                        //.join(Medicament_.GROUPE_MEDICAMENT).join(GroupeMedicament_.GROUPE_MEDICAMENT_ASSOC)
                        //.join(GroupeMedicamentPrincipeActif_.PRINCIPE_ACTIF);

                //Predicate predicate2 = criteriaBuilder.like(principeActifPath.get(PrincipeActif_.LIBELLE), "%"+recherche+"%");

                //predicates.add(predicate1);
            });

            principeActifFilter.ifPresent(principeActif -> {

                Join<PrincipeActif, Presentation> principeActifRoot = presentationRoot
                        .join("medicament")
                        .join("groupeMedicament")
                        .join("groupeMedicamentAssoc")
                        .join("principeActif");

                predicates.add(criteriaBuilder.like(principeActifRoot.get(PrincipeActif_.LIBELLE), "%"+principeActif+"%"));

            });

            estReferenceFilter.ifPresent( estReference -> {
                Path<Object> principeActifPath =
                        presentationRoot.join(Presentation_.MEDICAMENT);

                Predicate predicate1 = criteriaBuilder.equal(principeActifPath.get(Medicament_.EST_REFERENCE), estReference);
                predicates.add(predicate1);
            });

            estEnStockFilter.ifPresent( estEnStock -> {
                Predicate predicate1;
                if(estEnStock){
                    predicate1 = criteriaBuilder.gt(presentationRoot.get(Presentation_.QUANTITE_STOCK), 0);
                }else{
                    predicate1 = criteriaBuilder.le(presentationRoot.get(Presentation_.QUANTITE_STOCK), 0);
                }
                predicates.add(predicate1);
            });

            fabricantsFilter.ifPresent(fabricants -> {
                Join<Fabricant, Presentation> fabricantRoot = presentationRoot
                        .join("medicament")
                        .join("fabricants");

                predicates.add(criteriaBuilder.or(fabricants
                        .stream()
                        .map(nomFab -> criteriaBuilder.like(fabricantRoot.get(Fabricant_.LIBELLE),"%"+nomFab+"%"))
                        .toArray(Predicate[]::new)));
            });
            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
