package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.key.CommandePresentationKey;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import fr.uga.miage.m1.model.mapper.CommandeMapper;
import fr.uga.miage.m1.model.request.AjouterAuPanierDTO;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PanierService {
    private final CommandesRepository commandeRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final CommandeMapper commandeMapper;

    private Commande getOrCreatePanier(Utilisateur utilisateur) {
        Commande commande = commandeRepository.getPanier(utilisateur.getId()).orElse(null);
        if (commande == null) {
            commande = new Commande();
            commande.setEtat(EtatCommande.panier);
            commande.setUtilisateur(utilisateur);
            commande.setDateAchat(new Timestamp(System.currentTimeMillis()));
            commandeRepository.save(commande);
        }
        return commande;
    }

    public List<PanierPresentationDTO> getPanier(Utilisateur utilisateur) {
        return commandeMapper.panierToDto(getOrCreatePanier(utilisateur).getCommandePresentations());
    }

    public List<PanierPresentationDTO> addPresentationToPanier(AjouterAuPanierDTO dto, Utilisateur utilisateur) {
        Commande commande = this.getOrCreatePanier(utilisateur);
        Presentation pres = presentationsRepository.findById(Long.valueOf(dto.getPresentation_id())).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), pres.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElse(null);
        if (commandePresentation == null) {
            commandePresentation = new CommandePresentation();
            commandePresentation.setId(commandePresentationKey);
            commandePresentation.setQuantite(dto.getQuantite());
            commandePresentation.setEtat(EtatCommande.panier);
        } else {
            commandePresentation.setQuantite(commandePresentation.getQuantite() + dto.getQuantite());
        }

        commandesPresentationRepository.save(commandePresentation);

        return commandeMapper.panierToDto(commande.getCommandePresentations());
    }
}
