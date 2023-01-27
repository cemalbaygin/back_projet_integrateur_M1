package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Etablissement")
@Getter
@Setter
public class Etablissement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    @NotNull(message = "le libellé de l'établissement ne doit pas être null")
    private String libelle;

    @Column
    @NotNull(message = "une adresse doit e^tre renseignée")
    private String adresse;

    @Column
    private String numTelephone;

    @Column
    private String type;

    @Column
    private boolean estSurveillanceRenforcee;

    @OneToMany(mappedBy= "etablissement")
    private List<Utilisateur> utilisateurs;

}
