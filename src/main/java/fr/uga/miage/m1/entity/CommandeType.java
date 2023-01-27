package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "commandeType")
@Getter
@Setter
public class CommandeType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "le libellé d'une commandeType ne doit pas être null")
    private String libelle;

    @OneToMany(mappedBy = "commandeType")
    private List<PresentationCommandeType> presentationCommandeTypes;

    @ManyToOne
    private Utilisateur utilisateur;
}
