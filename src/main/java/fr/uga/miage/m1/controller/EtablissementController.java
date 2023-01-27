package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.model.dto.EtablissementDto;
import fr.uga.miage.m1.service.EtablissementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/etablissements")
@Tag(name = "Etablissement")
public class EtablissementController {

    private final EtablissementService etablissementService;

    @GetMapping
    public ResponseEntity<List<EtablissementDto>> getEtablissements(){
        return new ResponseEntity<>(etablissementService.getEtablissements(), HttpStatus.OK);
    }
}
