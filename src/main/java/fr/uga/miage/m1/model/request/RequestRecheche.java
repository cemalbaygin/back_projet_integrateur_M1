package fr.uga.miage.m1.model.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
public class RequestRecheche{
    public RequestRecheche() {
        this.nomMedicament= Optional.empty();
        this.principeActif= Optional.empty();
        this.estReference= Optional.empty();
        this.estEnStock= Optional.empty();
        this.fabricants= Optional.empty();

    }

    private Optional<String> nomMedicament;
    private Optional<String> principeActif;
    private Optional<Boolean> estReference;
    private Optional<Boolean> estEnStock ;
    private Optional<List<String>> fabricants;
}