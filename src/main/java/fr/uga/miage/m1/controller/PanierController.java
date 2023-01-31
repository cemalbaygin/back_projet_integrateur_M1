package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.request.AjouterAuPanierDTO;
import fr.uga.miage.m1.service.PanierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/panier")
@Tag(name = "Panier")
public class PanierController {

    private final PanierService service;

    @GetMapping
    public ResponseEntity<List<PanierPresentationDTO>> getPanier(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        return new ResponseEntity<>(service.getPanier(utilisateur), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> addToPanier(Authentication authentication,
                                               @RequestBody AjouterAuPanierDTO dto) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        return new ResponseEntity<>(service.addPresentationToPanier(utilisateur, dto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<List<PanierPresentationDTO>> deleteFromPanier(Authentication authentication,
                                                                        @RequestParam("code_CIP13") String codeCIP13) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        return new ResponseEntity<>(service.deleteFromPanier(utilisateur, codeCIP13), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<PanierPresentationDTO>> updateFromPanier(Authentication authentication,
                                                                        @RequestBody AjouterAuPanierDTO dto) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        return new ResponseEntity<>(service.updateFromPanier(utilisateur, dto), HttpStatus.OK);
    }

    @PostMapping("{codeCIP13}")
    public ResponseEntity<List<PresentationMedicamentDTO>> substituerProduit(@PathVariable("codeCIP13") String codeCIP13) {

        return new ResponseEntity<>(service.getSimilaires(codeCIP13), HttpStatus.OK);
    }

    @PostMapping("{codeCIP13}/substituer")
    public ResponseEntity<Boolean> substituerProduit(Authentication authentication,
                                                     @PathVariable("codeCIP13") String sourceCodeCIP13,
                                                     @RequestParam("destination") String destinationCodeCIP13) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        return new ResponseEntity<>(service.substituerProduit(utilisateur, sourceCodeCIP13, destinationCodeCIP13), HttpStatus.OK);
    }

    @PostMapping("/valider")
    public ResponseEntity<Boolean> passerCommande(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        return new ResponseEntity<>(service.passerCommande(utilisateur), HttpStatus.OK);
    }

    @PostMapping("/allPresentations")
    public ResponseEntity<Boolean> addAllToPanier(Authentication authentication,
                                               @RequestBody List<AjouterAuPanierDTO> dto) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        return new ResponseEntity<>(service.addPresentationsToPanier(utilisateur, dto), HttpStatus.OK);
    }


}