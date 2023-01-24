package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.key.GroupeMedicamentPrincipeActifKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "groupe_medicament_principe_actif")
@Setter
public class GroupeMedicamentPrincipeActif {
    @EmbeddedId
    GroupeMedicamentPrincipeActifKey id;

    @ManyToOne
    @MapsId("groupe_medicament_id")
    @JoinColumn(name = "groupe_medicament_id", referencedColumnName = "id")
    private GroupeMedicament groupeMedicament;

    @ManyToOne
    @MapsId("principe_actif_id")
    @JoinColumn(name = "principe_actif_id", referencedColumnName = "id")
    private PrincipeActif principeActif;

    @Column(length = 255)
    private String dosage;

}

