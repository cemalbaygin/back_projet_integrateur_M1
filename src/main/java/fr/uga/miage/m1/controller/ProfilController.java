package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.model.dto.ProfilPostDTO;
import fr.uga.miage.m1.model.dto.UtilisateurDTO;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UtilisateurDTO> getProfil(Authentication authentication) {
        UtilisateurDTO utilisateurDTO = userService.getProfil(authentication);
        return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> postProfil(Authentication authentication,@RequestBody ProfilPostDTO dto){
        return userService.changeMdp(authentication,dto);
    }
}
