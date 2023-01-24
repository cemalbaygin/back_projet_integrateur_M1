package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.compositeKey.CommandePresentationKey;
import fr.uga.miage.m1.model.compositeKey.GroupeMedicamentPrincipeActifKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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
