package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.PresentationsPaginatedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "public/presentations")
@RequiredArgsConstructor
public class PresentationController {
    private final PresentationsPaginatedRepository presentationRepo;
    private final PresentationMapper presentationMapper;

    @GetMapping
    public Normalizer index(@RequestParam("page") int page,
                            @RequestParam("size") int size) {

        Sort sort = Sort.by("libelle").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Presentation> presentations = presentationRepo.findAll(pageable);
        List<Presentation> res = presentations.getContent();

        List<PresentationDTO> collect = res.stream().map(e -> presentationMapper.entityToDto(e)).collect(Collectors.toList());

        Normalizer response = new Normalizer(collect, presentations);
        return response;
    }
}
