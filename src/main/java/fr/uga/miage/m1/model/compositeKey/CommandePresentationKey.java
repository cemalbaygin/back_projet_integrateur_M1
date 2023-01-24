package fr.uga.miage.m1.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CommandePresentationKey {
    @Column(name = "commande_id")
    Integer commandeId;

    @Column(name = "presentation_id")
    Long presentationId;

    private CommandePresentationKey() {
    }

    public CommandePresentationKey(Integer commandeId, Long presentationId) {
        this.commandeId = commandeId;
        this.presentationId = presentationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandePresentationKey that = (CommandePresentationKey) o;
        return Objects.equals(commandeId, that.commandeId) && Objects.equals(presentationId, that.presentationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, presentationId);
    }
}
