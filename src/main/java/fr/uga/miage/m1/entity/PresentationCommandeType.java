package fr.uga.miage.m1.entity;


import fr.uga.miage.m1.model.key.CommandeTypePresentationKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "presentationCommandeType")
@Getter
@Setter
public class PresentationCommandeType {
    @EmbeddedId
    CommandeTypePresentationKey id;

    @Column
    private Integer quantite;

    @ManyToOne
    @MapsId("commandeType_id")
    @JoinColumn(name = "commandeType_id", referencedColumnName = "id")
    private CommandeType commandeType;
    @ManyToOne
    @MapsId("presentation_id")
    @JoinColumn(name = "presentation_id", referencedColumnName = "codeCIP13")
    private Presentation presentation;
}
