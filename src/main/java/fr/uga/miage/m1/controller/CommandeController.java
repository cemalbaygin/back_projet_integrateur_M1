package fr.uga.miage.m1.controller;


import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.CommandeCompleteDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.service.CommandeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/commande")
@Tag(name = "Commande")
@Log
public class CommandeController {

    private final CommandeService commandeService;

    private final AutoMapper mapper;

    @GetMapping
    public ResponseEntity<List<CommandeCompleteDTO>> getListCommandes(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        List<CommandeCompleteDTO> commandesCompleteDTOS = commandeService.getListCommandes(utilisateur);

        return new ResponseEntity<>(commandesCompleteDTOS, HttpStatus.OK);
    }

    @DeleteMapping("{idCommande}")
    public ResponseEntity<Boolean> annulerCommande(@PathVariable Integer idCommande){
        return new ResponseEntity<>(commandeService.annulerEnAttente(idCommande), HttpStatus.OK);
    }
}
