package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.entity.Fabricant;
import fr.uga.miage.m1.entity.Medicament;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationDTO {
    Long codeCIP13;
    String libelle;
    Double prix;
    Integer tauxRemboursement;
    MedicamentDTO medicament;
}
