package fr.uga.miage.m1.model.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AjouterAuPanierDTO {
    String presentation_id;
    Integer quantite;
}