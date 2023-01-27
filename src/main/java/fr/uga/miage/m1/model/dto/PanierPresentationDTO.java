package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanierPresentationDTO {
    PresentationDTO presentation;
    MedicamentDTO medicament;
    private Integer quantite;
}
