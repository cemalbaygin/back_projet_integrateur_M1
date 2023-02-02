package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.EtatCommande;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "commande")
@Getter
@Setter
public class Commande {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Timestamp dateAchat;

    @Column
    @Enumerated(EnumType.STRING)
    private EtatCommande etat;

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY)
    private List<CommandePresentation> commandePresentations;

    @ManyToOne
    private Utilisateur utilisateur;
}
