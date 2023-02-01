package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.model.dto.FabricantDTO;
import fr.uga.miage.m1.service.FabricantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="public/fabricants")
@RequiredArgsConstructor
public class FabricantController {

    private final FabricantService fabricantService;

    @GetMapping
    public ResponseEntity<List<FabricantDTO>> getFabricants(
            @RequestParam("nomMedimament") Optional<String> nomMedicament,
            @RequestParam("principeActif") Optional<String> principeActif
    ){
        return new ResponseEntity<>(fabricantService.getFabricants(nomMedicament, principeActif), HttpStatus.OK);
    }
}
