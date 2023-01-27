package fr.uga.miage.m1.model.dto;

import fr.uga.miage.m1.entity.PresentationCommandeType;
import fr.uga.miage.m1.entity.Utilisateur;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommandeTypeDTO {

    private String libelle;

    private List<PresentationDTO> presentations;
    
}
