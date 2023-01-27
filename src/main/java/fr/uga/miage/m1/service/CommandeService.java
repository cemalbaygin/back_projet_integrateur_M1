package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommandeService {
    private final PanierService panierService;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final CommandesRepository commandesRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean passerCommande(Utilisateur user) {
        Commande panier = panierService.getOrCreatePanier(user);
        panier.setEtat(EtatCommande.expedier);

        List<CommandePresentation> commandePresentations = panier.getCommandePresentations();
        if (commandePresentations.size() == 0) throw new NoSuchElementException();

        for (int i = 0; i < commandePresentations.size(); i++) {
            CommandePresentation comPres = commandePresentations.get(i);
            Presentation pres = comPres.getPresentation();

            comPres.setEtat(EtatCommande.en_cours);

            if (pres.getQuantiteStock() >= comPres.getQuantite()) {
                pres.setQuantiteStock(pres.getQuantiteStock() - comPres.getQuantite());
                comPres.setEtat(EtatCommande.expedier);
                presentationsRepository.save(pres);
            } else {
                panier.setEtat(EtatCommande.en_cours);
            }

            commandesPresentationRepository.save(comPres);
        }

        commandesRepository.save(panier);

        return true;
    }
}
