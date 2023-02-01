package fr.uga.miage.m1.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UtilisateurDTO {
    private String firstname;
    private String lastname;
    private String email;
    private EtablissementDto etablissementDto;
}
