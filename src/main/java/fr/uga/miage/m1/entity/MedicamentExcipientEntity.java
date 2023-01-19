package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import fr.uga.miage.m1.model.MedicamentExcipientKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "medicament_excipients")
@Setter
public class MedicamentExcipientEntity {
    @EmbeddedId
    MedicamentExcipientKey id;

    @ManyToOne
    @MapsId("medicament_id")
    @JoinColumn(name = "medicament_id", referencedColumnName = "codeCIS")
    private MedicamentEntity medicament;

    @MapsId("excipient_id")
    @ManyToOne
    @JoinColumn(name = "excipient_id", referencedColumnName = "id")
    private ExcipientEntity excipient;

    @Column(length = 255)
    private String dosage;
}
