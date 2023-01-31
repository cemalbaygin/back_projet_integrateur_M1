package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "public/presentations")
@RequiredArgsConstructor
public class PresentationController {

    private final PresentationService presentationService;

    @GetMapping
    public ResponseEntity<Page<PresentationMedicamentDTO>> index(@RequestParam("nom") Optional<String> nomMedicament,
                                                                 @RequestParam("principeActif") Optional<String> principeActif,
                                                                 @RequestParam("estReference") Optional<Boolean> estReference,
                                                                 @RequestParam("estEnStock") Optional<Boolean> estEnStock,
                                                                 @ParameterObject @PageableDefault Pageable pageable) {

        Page<PresentationMedicamentDTO> presentations = presentationService.getPresentationsWithFilter(nomMedicament,principeActif , estReference,estEnStock, pageable);

        return new ResponseEntity<>(presentations, HttpStatus.OK);
    }

    @GetMapping("{codeCIP13}")
    public ResponseEntity<PresentationCompleteDTO> show(@PathVariable Long codeCIP13) {
        try{
            return new ResponseEntity<PresentationCompleteDTO>(presentationService.getPresentation(codeCIP13), HttpStatus.OK);
        }catch(NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
