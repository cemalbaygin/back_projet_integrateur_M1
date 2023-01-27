package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Utilisateur;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommandeService {
    private final PanierService panierService;

    @Transactional
    public boolean passerCommande(Utilisateur user) {
        Commande panier = panierService.getOrCreatePanier(user);

        List<CommandePresentation> presentations = panier.getCommandePresentations();

        if (presentations.size() == 0) throw new NoSuchElementException();

        for (int i = 0; i < presentations.size(); i++) {

        }

        return true;
    }
}
