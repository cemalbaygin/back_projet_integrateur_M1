package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupeMedicamentDTO {
    String libelle;
    List<PresentationMedicamentDTO> presentationsMedicaments;
    List<PrincipeActifDTO> principesActifs;
}
