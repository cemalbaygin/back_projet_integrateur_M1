package fr.uga.miage.m1.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GroupeMedicamentPrincipeActifKey {
    @Column(name = "groupe_medicament_id")
    Long groupeMedicamentId;

    @Column(name = "principe_actif_id")
    Long principeActifId;

    private GroupeMedicamentPrincipeActifKey() {
    }

    public GroupeMedicamentPrincipeActifKey(Long groupeMedicamentId, Long principeActifId) {
        this.groupeMedicamentId = groupeMedicamentId;
        this.principeActifId = principeActifId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupeMedicamentPrincipeActifKey that = (GroupeMedicamentPrincipeActifKey) o;
        return Objects.equals(groupeMedicamentId, that.groupeMedicamentId) && Objects.equals(principeActifId, that.principeActifId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupeMedicamentId, principeActifId);
    }
}
