package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.model.EtatCommande;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommandeDTO {
    Long id;
    private Timestamp dateAchat;
    private EtatCommande etat;
}