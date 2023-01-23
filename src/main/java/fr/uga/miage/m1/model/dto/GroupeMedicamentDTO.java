package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.entity.GroupeMedicamentPrincipeActif;
import fr.uga.miage.m1.entity.PrincipeActif;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapping;

import java.util.List;

@Getter
@Setter
public class GroupeMedicamentDTO {
    String libelle;
    List<MedicamentDTO> medicaments;
    List<PrincipeActifDTO> principesActifs;
}
