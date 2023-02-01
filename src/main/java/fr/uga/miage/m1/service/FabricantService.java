package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.FabricantDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.repository.FabricantsRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FabricantService {

    private final FabricantsRepository fabricantsRepository;
    private final AutoMapper mapper;

    public List<FabricantDTO> getFabricants (Optional<String> nomMedicament , Optional<String> principeActif){
        return fabricantsRepository.findAll(buildSpecifications(nomMedicament, principeActif),Sort.by(Sort.Direction.ASC, "libelle"))
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    private Specification<Fabricant> buildSpecifications(Optional<String> nomMedicamentFilter , Optional<String> principeActifFilter){

        return (fabricantRoot, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //filtre sur la recherche
            nomMedicamentFilter.ifPresent(nomMedicament -> {

                Join<Medicament, Fabricant> medicament = fabricantRoot.join("medicaments");
                predicates.add(criteriaBuilder.like(medicament.get(Medicament_.LIBELLE), "%"+nomMedicament+"%"));

            });

            principeActifFilter.ifPresent(principeActif -> {

                Join<PrincipeActif, Fabricant> pricipeActifRoot = fabricantRoot
                        .join("medicaments")
                        .join("groupeMedicament")
                        .join("groupeMedicamentAssoc")
                        .join("principeActif");

                predicates.add(criteriaBuilder.like(pricipeActifRoot.get(PrincipeActif_.LIBELLE), "%"+principeActif+"%"));

            });
            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
