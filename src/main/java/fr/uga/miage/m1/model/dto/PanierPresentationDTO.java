package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.entity.Prescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PanierPresentationDTO {
    PresentationDTO presentation;
    MedicamentDTO medicament;
    private Integer quantite;
    List<Prescription> prescriptions;
}
