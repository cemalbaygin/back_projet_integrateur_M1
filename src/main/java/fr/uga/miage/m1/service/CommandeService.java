package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
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
                        .presentation(mapper.entityToDto(cp.getPresentation()))
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

}
