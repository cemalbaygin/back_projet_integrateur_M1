package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "groupe_medicament_principe_actif")
@Setter
public class GroupeMedicamentPrincipeActifEntity {
    @EmbeddedId
    GroupeMedicamentPrincipeActifKey id;

    @ManyToOne
    @MapsId("groupe_medicament_id")
    @JoinColumn(name = "groupe_medicament_id", referencedColumnName = "id")
    private GroupeMedicamentEntity groupeMedicament;

    @ManyToOne
    @MapsId("principe_actif_id")
    @JoinColumn(name = "principe_actif_id", referencedColumnName = "id")
    private PrincipeActifEntity principeActif;

    @Column(length = 255)
    private String dosage;

}

