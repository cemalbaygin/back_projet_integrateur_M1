package fr.uga.miage.m1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EtablissementDto {

    @Schema(description="Identifiant de l'établissement")
    @NotNull(message = "id etablissement ne doit pas être null")
    @Positive(message = "L'id doit être positif")
    private Integer id;

    @Schema(description="Le libellé de l'établissement")
    @NotNull(message = "le libellé de l'établissement ne doit pas être null")
    private String libelle;

    @Schema(description="L'adresse de l'établissement")
    @NotNull(message = "l'adresse de l'établissement ne doit pas être nulle")
    private String adresse;

    @Schema(description="Numéro de téléphone de l'établissement")
    private String numTelephone;

    @Schema(description="Type de l'établisement", example = "hôpital, clinique")
    private String type;

    @Schema(description="Si l'établissement est sous surveillance renforcée")
    private boolean estSurveillanceRenforcee;

}
