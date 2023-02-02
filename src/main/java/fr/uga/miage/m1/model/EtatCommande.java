package fr.uga.miage.m1.model;

/**
 * Différent etat de la commande/d'un element de la commande
 * <p>
 * Panier => le prix est suceptible de bouger, aucun impact sur les quantités en stock
 * <p>
 * attente_paiement => le prix est fixé, produit pas en stock et en attente de paiement/facturation
 * <p>
 * attente_paiement_reserver => le prix est fixé, le produit est "mis de coté", en attente de paiement/facturation
 * <p>
 * en_cours => le prix est fixé, produit pas en stock en attente de restockage
 * <p>
 * annuler => les produits reservés sont remis en stock avec trace de l'annulation.
 */
public enum EtatCommande {
    panier, // la commande/le produit est dans/le panier
    attente_paiement,  // la commande/le produit est en attente de paiement/facture et en attente de restockage
    attente_paiement_reserver, // la commande/le produit est en attente de paiement/facture mais est reserver
    en_cours, // la commande/le produit n'a pas été expedier entierement
    expedier, // la commande/le produit as été expedier
    annuler


}