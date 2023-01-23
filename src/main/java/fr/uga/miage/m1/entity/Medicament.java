package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "medicament")
@Setter
public class Medicament {
    @Id
    @Column(name = "codecis")
    private Long codeCIS;

    @Column(length = 512)
    @NonNull
    private String libelle;

    // TODO Verifier que il ny as pas de doublons avec Presentation
    @Column(length = 255)
    @NonNull
    private String formePharmaceutique;

    @ManyToMany
    @NonNull
    @JoinTable(name = "MedicamentFabricants", joinColumns = @JoinColumn(name = "codeCIS"), inverseJoinColumns = @JoinColumn(name = "id"))
    List<Fabricant> fabricants;

    @Column
    @NonNull
    private Boolean aSurveillanceRenforce;

    // TODO attention not nullable plus tard
    @Column
    private Boolean estReference;

    // TODO attention not nullable plus tard
    @ManyToOne
    private GroupeMedicament groupeMedicament;

    @ManyToMany
    @JoinTable(name = "MedicamentTypesAdministration", joinColumns = @JoinColumn(name = "codeCIS"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<TypeAdministration> typesAdministration;

    @OneToMany(mappedBy = "medicament")
    private List<MedicamentExcipient> medicamentAssos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MedicamentPrescription", joinColumns = @JoinColumn(name = "codeCIS"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<Prescription> prescriptions;
}
 