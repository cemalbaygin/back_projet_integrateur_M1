package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presentation")
@DynamicUpdate
public class Presentation {
    @Id
    private Long codeCIP13;

    @NonNull
    @Column(length = 512)
    private String libelle;

    @NonNull
    @Column
    private Double prix;

    @NonNull
    private Integer tauxRemboursement;

    @ManyToOne
    private Medicament medicament;

    @OneToMany(mappedBy = "presentation")
    private List<CommandePresentation> commandePresentations;

    @Column
    @NonNull
    private Integer quantiteStock;

    @Column
    @OneToMany(mappedBy = "presentation")
    private List<PresentationCommandeType> presentationCommandeTypes;
}