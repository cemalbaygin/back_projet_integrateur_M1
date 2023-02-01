package fr.uga.miage.m1.model.request;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RequestRecheche{
    public Optional<String> nomMedicament = Optional.empty();
    public Optional<String> principeActif = Optional.empty();
    public Optional<Boolean> estReference = Optional.empty();
    public Optional<Boolean> estEnStock = Optional.empty();
    public Optional<List<String>> fabricants = Optional.empty();
}