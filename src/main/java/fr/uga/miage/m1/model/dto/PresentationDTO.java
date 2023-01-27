package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationDTO {
    Long codeCIP13;
    String libelle;
    Double prix;
    Integer tauxRemboursement;
    Integer quantiteStock;
}
