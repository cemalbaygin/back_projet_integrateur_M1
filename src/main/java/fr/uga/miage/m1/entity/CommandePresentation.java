package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.key.CommandePresentationKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "commande_presentation")
@Getter
@Setter
public class CommandePresentation {
    @EmbeddedId
    CommandePresentationKey id;

    @Column
    private Double prixAchat;

    @Column
    private Integer quantite;

    @Column
    @Enumerated(EnumType.STRING)
    private EtatCommande etat;

    @ManyToOne
    @MapsId("commande_id")
    @JoinColumn(name = "commande_id", referencedColumnName = "id")
    private Commande commande;

    @ManyToOne
    @MapsId("presentation_id")
    @JoinColumn(name = "presentation_id", referencedColumnName = "codeCIP13")
    private Presentation presentation;
}
