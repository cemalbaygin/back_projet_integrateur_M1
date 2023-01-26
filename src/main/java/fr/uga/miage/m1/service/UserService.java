package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.ProfilPostDTO;
import fr.uga.miage.m1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;


    public Utilisateur changeMdp(Utilisateur utilisateur, ProfilPostDTO profil){
        if(profil.getPassword()!=null){
            utilisateur.setPassword(passwordEncoder.encode(profil.getPassword()));
        }
        utilisateur.setEmail(profil.getEmail());
        utilisateur.setFirstname(profil.getFirstname());
        utilisateur.setLastname(profil.getLastname());
        userRepo.save(utilisateur);
        return utilisateur;
    }
}
