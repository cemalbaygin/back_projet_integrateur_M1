package fr.uga.miage.m1.service;

import fr.uga.miage.m1.auth.AuthenticationRequest;
import fr.uga.miage.m1.auth.AuthenticationService;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.ProfilPostDTO;
import fr.uga.miage.m1.model.dto.UtilisateurDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Log
public class UserService {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AutoMapper mapper;




    public ResponseEntity<UtilisateurDTO> changeMdp(Authentication authentication, ProfilPostDTO profil){
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        if (!profil.getEmail().equals(utilisateur.getEmail())) {
            utilisateur = userRepo.findByEmail(profil.getEmail().toLowerCase()).orElse(null);
            if(utilisateur!=null){
                utilisateur=null;
                return  new ResponseEntity<>(mapper.entityToDto(utilisateur), HttpStatus.NOT_ACCEPTABLE);
            }
        }
            utilisateur = (Utilisateur) authentication.getPrincipal();
            utilisateur.setPassword(passwordEncoder.encode(profil.getPassword()));
            utilisateur.setEmail(profil.getEmail());
            utilisateur.setFirstname(profil.getFirstname());
            utilisateur.setLastname(profil.getLastname());
            userRepo.save(utilisateur);

            Authentication result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getEmail(),
                            profil.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(result);

        log.info(""+utilisateur);
        return new ResponseEntity<>(mapper.entityToDto(utilisateur), HttpStatus.OK);
    }
}
