package fr.uga.miage.m1.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PresentationForCommandeTypeDTO {
    private Long codeCIP13;
    private String libelle;
    private String libelleMedicament;
    private Integer quantite;
}
