package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
import fr.uga.miage.m1.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "public/presentations")
@RequiredArgsConstructor
public class PresentationController {

    private final PresentationService presentationService;

    private final PresentationsRepository presentationRepo;
    private final AutoMapper autoMapper;
    private final PresentationMapper presentationMapper;

    @GetMapping
    public Normalizer index(@RequestParam("search") Optional<String> recherche,
                            @ParameterObject @PageableDefault Pageable pageable) {

        //System.out.println(page);
        //System.out.println(size);
        //Sort sort = Sort.by(sortBy.orElse("libelle")).descending();
        //Pageable pageable = PageRequest.of(page, size);

        return presentationService.getPresentationsWithFilter(recherche, pageable);
    }

    @GetMapping("{codeCIP13}")
    public PresentationCompleteDTO show(@PathVariable Long codeCIP13) {
        Presentation presentation = presentationRepo.findById(Long.valueOf(codeCIP13)).orElseThrow();

        return presentationMapper.entityToDto(presentation);
    }
}
