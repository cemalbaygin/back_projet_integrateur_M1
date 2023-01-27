package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.model.EtatCommande;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommandePresentationDTO {
    PresentationDTO presentation;
    private Double prixAchat;
    private Integer quantite;
    private EtatCommande etat;
}
