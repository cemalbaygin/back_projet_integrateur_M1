package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.dto.CommandeCompleteDTO;
import fr.uga.miage.m1.model.dto.CommandePresentationDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import fr.uga.miage.m1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Log
public class CommandeService {

    private final UserRepository userRepository;
    private final CommandesRepository commandesRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;


    private final AutoMapper mapper;
    private final PresentationMapper presentationMapper;

    public List<CommandeCompleteDTO> getListCommandes(Utilisateur utilisateur) {
        List<Commande> commandes = userRepository.findByEmail(utilisateur.getEmail()).orElseThrow().getCommandes();
        List<Commande> commandes1 = new ArrayList<>();

        for (Commande c : commandes) {
            if (c.getEtat() != EtatCommande.panier) {
                commandes1.add(c);
            }
        }

        List<CommandeCompleteDTO> commandeCompleteDTOS = new ArrayList<>();
        for (Commande c : commandes1) {
            List<CommandePresentationDTO> presentationDTOS = new ArrayList<>();
            for (CommandePresentation cp : c.getCommandePresentations()) {
                var presentation = CommandePresentationDTO.builder()
                        .presentationMedicament(presentationMapper.presentationMedicamentDTO(cp.getPresentation()))
                        .etat(cp.getEtat())
                        .prixAchat(cp.getPrixAchat())
                        .quantite(cp.getQuantite())
                        .build();
                presentationDTOS.add(presentation);
            }
            var commandeCompleteDTO = CommandeCompleteDTO.builder()
                    .presentations(presentationDTOS)
                    .commandeDTO(mapper.entityToDto(c))
                    .build();
            commandeCompleteDTOS.add(commandeCompleteDTO);
        }
        return commandeCompleteDTOS;
    }

    public void expedierEnAttente() {
        List<Commande> commandesEnAttente = commandesRepository.getCommandesEnAttente();

        log.info("Expedition des commandes - " + commandesEnAttente.size());

        for (Commande commande : commandesEnAttente) {
            List<CommandePresentation> commandePresentations = commande.getCommandePresentations();
            commande.setEtat(EtatCommande.expedier);

            for (int i = 0; i < commandePresentations.size(); i++) {
                CommandePresentation comPres = commandePresentations.get(i);

                if (comPres.getEtat() == EtatCommande.attente_paiement_reserver) {
                    comPres.setEtat(EtatCommande.expedier);
                } else {
                    comPres.setEtat(EtatCommande.en_cours);
                    commande.setEtat(EtatCommande.en_cours);
                }

                commandesPresentationRepository.save(comPres);
            }

            commandesRepository.save(commande);
        }
        log.info("Expedition des commandes - end");
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
    public boolean annulerEnAttente(Integer idCommande){
        Commande c = commandesRepository.findById(idCommande).orElseThrow();
        if(c.getEtat() != EtatCommande.attente_paiement) throw new NoSuchElementException();

        List<CommandePresentation> commandePresentations = c.getCommandePresentations();
        c.setEtat(EtatCommande.annuler);

        for (int i = 0; i < commandePresentations.size(); i++) {
            CommandePresentation comPres = commandePresentations.get(i);

            if (comPres.getEtat() == EtatCommande.attente_paiement_reserver) {
                Presentation pres = comPres.getPresentation();
                pres.setQuantiteStock(pres.getQuantiteStock() + comPres.getQuantite());
                presentationsRepository.save(pres);
            }

            comPres.setEtat(EtatCommande.annuler);
            commandesPresentationRepository.save(comPres);
        }

        commandesRepository.save(c);

        return true;
    }
}