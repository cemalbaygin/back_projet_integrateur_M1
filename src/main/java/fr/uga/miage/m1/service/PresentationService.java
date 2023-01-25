package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Presentation_;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
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


    public Normalizer getPresentationsWithFilter(Optional<String> libelle, Optional<Integer> prix, Pageable paging) {
        Specification<Presentation> specification = buildSpecifications(libelle, prix);
        Page<Presentation> presentations = presentationRepo.findAll(specification,paging);
        System.out.println(presentations.getContent().size());
        List<Presentation> res = presentations.getContent();

        List<PresentationDTO> collect = res.stream().map(e -> autoMapper.entityToDto(e)).collect(Collectors.toList());

        return new Normalizer(collect, presentations);


    }

    private Specification<Presentation> buildSpecifications(Optional<String> libelleFilter, Optional<Integer> prixFilter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //filtre sur libelle
            libelleFilter.ifPresent(libelle -> {
                System.out.println(libelle);
                predicates.add(criteriaBuilder.like(root.get(Presentation_.LIBELLE), libelle));
            });
            prixFilter.ifPresent(prix -> {
                System.out.println(prix);
                predicates.add(criteriaBuilder.equal(root.get(Presentation_.TAUX_REMBOURSEMENT), prix));
            });
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
