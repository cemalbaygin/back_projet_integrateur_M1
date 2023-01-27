package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PresentationCompleteDTO {
    List<PresentationDTO> presentations;
    MedicamentDTO medicament;
    List<ExcipientDTO> excipients;
    GroupeMedicamentDTO groupeMedicament;
    List<PrescriptionDTO> prescriptions;
}
