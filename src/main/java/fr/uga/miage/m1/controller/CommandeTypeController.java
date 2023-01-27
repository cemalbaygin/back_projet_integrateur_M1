package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.CommandeType;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.CommandeTypeDTO;
import fr.uga.miage.m1.model.dto.EtablissementDto;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.service.CommandeTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/CommandeType")
@Tag(name = "CommandeType")
public class CommandeTypeController {

    private final AutoMapper mapper;

    private final CommandeTypeService commandeTypeService;

    @GetMapping
  public ResponseEntity<List<CommandeTypeDTO>> getCommandeType(Authentication authentication){
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        List<CommandeType> commandeTypes = commandeTypeService.getListCommandeType(utilisateur);
        List<CommandeTypeDTO> commandeTypeDTOS = new ArrayList<>();
        for (CommandeType c: commandeTypes) {
            commandeTypeDTOS.add(mapper.entityTdto(c));
        }
        return new ResponseEntity<>(commandeTypeDTOS, HttpStatus.OK);
  }
}
