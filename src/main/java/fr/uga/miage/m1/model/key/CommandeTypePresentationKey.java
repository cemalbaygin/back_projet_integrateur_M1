package fr.uga.miage.m1.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CommandeTypePresentationKey {

    @Column(name = "commandeType_id")
    Integer commandeTypeId;

    @Column(name = "presentation_id")
    Long presentationId;

    private CommandeTypePresentationKey() {
    }

    public CommandeTypePresentationKey(Integer commandeTypeId, Long presentationId) {
        this.commandeTypeId = commandeTypeId;
        this.presentationId = presentationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandeTypePresentationKey that = (CommandeTypePresentationKey) o;
        return Objects.equals(commandeTypeId, that.commandeTypeId) && Objects.equals(presentationId, that.presentationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeTypeId, presentationId);
    }
}
