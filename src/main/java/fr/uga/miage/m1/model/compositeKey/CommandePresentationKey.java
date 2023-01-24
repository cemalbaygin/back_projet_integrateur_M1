package fr.uga.miage.m1.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CommandePresentationKey {
    @Column(name = "commande_id")
    Long commandeId;

    @Column(name = "presentation_id")
    Long presentation_id;

    private CommandePresentationKey() {
    }

    public CommandePresentationKey(Long commandeId, Long presentation_id) {
        this.commandeId = commandeId;
        this.presentation_id = presentation_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandePresentationKey that = (CommandePresentationKey) o;
        return Objects.equals(commandeId, that.commandeId) && Objects.equals(presentation_id, that.presentation_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, presentation_id);
    }
}
