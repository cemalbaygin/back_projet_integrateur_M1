package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.dto.CommandeCompleteDTO;
import fr.uga.miage.m1.model.dto.CommandePresentationDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import fr.uga.miage.m1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Log
public class CommandeService {

    private final CommandesRepository commandesRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final PanierService panierService;

    private final UserRepository userRepository;

    private final AutoMapper mapper;

    public void passerCommande(Utilisateur user) {
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
    }

    public List<CommandeCompleteDTO> getListCommandes(Utilisateur utilisateur) {
        log.info("Avant get commandes");
        List<Commande> commandes = userRepository.findByEmail(utilisateur.getEmail()).orElse(null).getCommandes();
        log.info("Après get commandes");
        List<Commande> commandes1 = new ArrayList<>();

        log.info("Avant filtre list commandes (panier): ");
        for (Commande c : commandes) {
            if (c.getEtat() != EtatCommande.panier) {
                commandes1.add(c);
            }
        }
        log.info("Après filtre list commandes (panier): ");

        List<CommandeCompleteDTO> commandeCompleteDTOS = new ArrayList<>();
        for (Commande c : commandes1) {
            log.info("id de la commande: " + c.getId());
            List<CommandePresentationDTO> presentationDTOS = new ArrayList<>();
            for (CommandePresentation cp : c.getCommandePresentations()) {
                var presentation = CommandePresentationDTO.builder()
                        .presentation(mapper.entityToDto(cp.getPresentation()))
                        .etat(cp.getEtat())
                        .prixAchat(cp.getPrixAchat())
                        .quantite(cp.getQuantite())
                        .build();
                presentationDTOS.add(presentation);
                log.info("presention : ");
                log.info("Libellé de la presentation :" + cp.getPresentation().getLibelle());
                log.info("Quantite de la presentation :" + cp.getQuantite());
                log.info("-----------");
            }
            var commandeCompleteDTO = CommandeCompleteDTO.builder()
                    .presentations(presentationDTOS)
                    .commandeDTO(mapper.entityToDto(c))
                    .build();
            commandeCompleteDTOS.add(commandeCompleteDTO);
            log.info("total presentation de la commande: " + c.getCommandePresentations().size());
        }
        return commandeCompleteDTOS;
    }

}
