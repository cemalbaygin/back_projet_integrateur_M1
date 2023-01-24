package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.compositeKey.CommandePresentationKey;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import fr.uga.miage.m1.model.mapper.CommandeMapper;
import fr.uga.miage.m1.model.requestDTO.AjouterAuPanierDTO;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/panier")
@Tag(name = "Panier")
public class PanierController {
    private final CommandesRepository commandeRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final CommandeMapper commandeMapper;

    @GetMapping
    public ResponseEntity<List<PanierPresentationDTO>> getPanier(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        Commande commande = this.getOrCreatePanier(utilisateur);

        return new ResponseEntity<List<PanierPresentationDTO>>(commandeMapper.panierToDto(commande.getCommandePresentations()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<PanierPresentationDTO>> addToPanier(Authentication authentication,
                                                                   @RequestBody AjouterAuPanierDTO dto) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        Commande commande = this.getOrCreatePanier(utilisateur);
        Presentation pres = presentationsRepository.findById(Long.valueOf(dto.getPresentation_id())).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), pres.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElse(null);
        if (commandePresentation == null) {
            commandePresentation = new CommandePresentation();
            commandePresentation.setId(commandePresentationKey);
            commandePresentation.setQuantite(dto.getQuantite());
            commandePresentation.setEtat(EtatCommande.panier);
        } else {
            commandePresentation.setQuantite(commandePresentation.getQuantite() + dto.getQuantite());
        }

        commandesPresentationRepository.save(commandePresentation);

        return new ResponseEntity<List<PanierPresentationDTO>>(commandeMapper.panierToDto(commande.getCommandePresentations()), HttpStatus.OK);
    }

    private Commande getOrCreatePanier(Utilisateur utilisateur) {
        Commande commande = commandeRepository.getPanier(utilisateur.getId()).orElse(null);
        if (commande == null) {
            commande = new Commande();
            commande.setEtat(EtatCommande.panier);
            commande.setUtilisateur(utilisateur);
            commande.setDateAchat(new Timestamp(System.currentTimeMillis()));
            commandeRepository.save(commande);
        }
        return commande;
    }

}