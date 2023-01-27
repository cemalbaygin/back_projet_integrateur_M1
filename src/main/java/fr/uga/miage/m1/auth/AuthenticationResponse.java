package fr.uga.miage.m1.auth;

import fr.uga.miage.m1.model.dto.UtilisateurDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String token;

  private UtilisateurDTO utilisateur;
}
