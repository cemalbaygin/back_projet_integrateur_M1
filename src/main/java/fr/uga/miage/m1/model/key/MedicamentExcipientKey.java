package fr.uga.miage.m1.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MedicamentExcipientKey {
    @Column(name = "medicament_id")
    Long medicamentId;

    @Column(name = "excipient_id")
    Long excipientId;

    private MedicamentExcipientKey() {
    }

    public MedicamentExcipientKey(Long groupeMedicamentId, Long principeActifId) {
        this.medicamentId = groupeMedicamentId;
        this.excipientId = principeActifId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicamentExcipientKey that = (MedicamentExcipientKey) o;
        return Objects.equals(medicamentId, that.medicamentId) && Objects.equals(excipientId, that.excipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId, excipientId);
    }
}
