package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicamentDTO {
    Long codeCIS;
    String libelle;
    String formePharmaceutique;
    List<FabricantDTO> fabricants;
    Boolean estReference;
    Boolean aSurveillanceRenforce;
}
