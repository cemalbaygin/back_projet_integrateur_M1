package fr.uga.miage.m1.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CommandeTypeDTO {

    private String libelle;

    private List<PresentationForCommandeTypeDTO> presentations;
    
}
