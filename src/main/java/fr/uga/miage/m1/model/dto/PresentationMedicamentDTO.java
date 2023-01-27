package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationMedicamentDTO {
    PresentationDTO presentation;
    MedicamentDTO medicament;
}