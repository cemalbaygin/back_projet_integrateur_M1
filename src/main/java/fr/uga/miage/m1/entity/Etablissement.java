package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Etablissement")
@Getter
@Setter
public class Etablissement {

    @Id
    @Column(name="id_etablissement")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="libelle")
    @NotNull(message = "le libellé de l'établissement ne doit pas être null")
    private String libelle;

    @Column(name="adresse")
    @NotNull(message = "une adresse doit e^tre renseignée")
    private String adresse;

    @Column(name="numero_telephone")
    private String numTelephone;

    @Column(name="type")
    private String type;

    @Column(name="estSurveillanceRenforcee")
    private boolean estSurveillanceRenforcee;
}
