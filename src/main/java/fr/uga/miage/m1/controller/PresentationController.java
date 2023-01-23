package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "public/presentations")
@RequiredArgsConstructor
public class PresentationController {
    private final PresentationsRepository presentationRepo;
    private final AutoMapper autoMapper;
    private final PresentationMapper presentationMapper;

    @GetMapping
    public Normalizer index(@RequestParam("page") int page,
                            @RequestParam("size") int size,
                            @RequestParam(value = "libelle") Optional<String> sortBy) {

        Sort sort = Sort.by("libelle").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Presentation> presentations = presentationRepo.findAll(pageable);
        List<Presentation> res = presentations.getContent();

        List<PresentationDTO> collect = res.stream().map(e -> autoMapper.entityToDto(e)).collect(Collectors.toList());

        Normalizer response = new Normalizer(collect, presentations);
        return response;
    }

    @GetMapping("{codeCIP13}")
    public PresentationCompleteDTO show(@PathVariable Long codeCIP13) {
        Presentation presentation = presentationRepo.findById(Long.valueOf(codeCIP13)).orElseThrow();

        return presentationMapper.entityToDto(presentation);
    }
}
