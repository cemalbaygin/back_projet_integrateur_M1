package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.ProfilPostDTO;
import fr.uga.miage.m1.model.dto.UtilisateurDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/profil")
@Tag(name = "Profil")
@Log
public class ProfilController {
    private final AutoMapper mapper;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UtilisateurDTO> getProfil(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        return new ResponseEntity<>(mapper.entityToDto(utilisateur), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> postProfil(Authentication authentication,@RequestBody ProfilPostDTO dto){
        log.info("lastname "+dto.getLastname()+"firstname "+dto.getFirstname()+"gmail "+dto.getEmail());
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        utilisateur= userService.changeMdp(utilisateur,dto);
        return new ResponseEntity<>(mapper.entityToDto(utilisateur), HttpStatus.OK);
    }
}
