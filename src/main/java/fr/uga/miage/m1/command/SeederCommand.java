package fr.uga.miage.m1.command;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import fr.uga.miage.m1.model.MedicamentExcipientKey;
import fr.uga.miage.m1.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Component
public class SeederCommand implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SeederCommand.class);

    @Autowired
    GroupeMedicamentsRepository groupeMedicamentsRepository;
    @Autowired
    FabricantsRepository fabricantsRepository;
    @Autowired
    MedicamentsRepository medicamentsRepository;
    @Autowired
    TypesAdministrationRepository typesAdministrationRepository;
    @Autowired
    GroupeMedicamentsPrincipeActifRepository groupeMedicamentsPrincipeActifRepository;
    @Autowired
    PrincipesActifsRepository principesActifsRepository;
    @Autowired
    MedicamentsExcipientsRepository medicamentsExcipientsRepository;
    @Autowired
    ExcipientsRepository excipientsRepository;
    @Autowired
    PresentationsRepository presentationsRepository;
    @Autowired
    PrescriptionsRepository prescriptionsRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Import start");

        //importMedicament();
        //importGroupeMedicament();

        //importComposition();

        //importPresentation();

        //importPrescription();

        logger.info("Import end");
    }

    private void importPrescription() throws IOException {
        logger.info("Import presentations");

        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_CPD_bdpm.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;

        int colCodeCis = 0;
        int colPrescription = 1;

        while ((line = reader.readLine()) != null) {
            counter++;
            if (counter % 1000 == 0) logger.info((counter * 100 / 25000) + "%");

            String[] columns = line.split("\t");


            Medicament med = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis])).orElse(null);
            if (med == null) continue;

            Prescription prescription;
            if (!prescriptionsRepository.existsByLibelle(columns[colPrescription])) {
                prescription = new Prescription();
                prescription.setLibelle(columns[colPrescription]);
                prescriptionsRepository.save(prescription);
            } else {
                prescription = prescriptionsRepository.getByLibelle(columns[colPrescription]);
            }

            if (!med.getPrescriptions().contains(prescription)) {
                med.getPrescriptions().add(prescription);
            }

            prescriptionsRepository.save(prescription);
            medicamentsRepository.save(med);
        }

    }

    private void importPresentation() throws IOException {
        logger.info("Import presentations");

        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_CIP_bdpm.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;

        int colCodeCis = 0;
        int colLibelle = 2;
        int colCodeCip13 = 6;
        int colPrix = 9;
        int colTauxRemboursement = 8;

        while ((line = reader.readLine()) != null) {
            counter++;
            String[] columns = line.split("\t");

            // if missing price
            if (columns.length < 10 || columns[colPrix].isBlank()) continue;

            Medicament med = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis])).orElse(null);
            if (med == null) continue;
            Long codeCip13 = Long.parseLong(columns[colCodeCip13]);

            Presentation pres = presentationsRepository.findById(codeCip13).orElse(null);
            if (pres != null) continue;

            if (counter % 1000 == 0) logger.info((counter * 100 / 20000) + "%");

            try {
                String l = columns[colPrix].replace(',', '.');

                int count = l.length() - l.replace(".", "").length();
                while (count > 1) {
                    l = l.replaceFirst(".", "");
                    count = l.length() - l.replace(".", "").length();
                }

                Double prix = Double.valueOf(l);

                String txs = columns[colTauxRemboursement].replace("%", "").trim();
                Integer tx = Integer.valueOf(txs);

                Presentation presentation = new Presentation();
                presentation.setCodeCIP13(codeCip13);
                presentation.setMedicament(med);
                presentation.setPrix(prix);
                presentation.setLibelle(columns[colLibelle]);
                presentation.setTauxRemboursement(tx);

                presentationsRepository.save(presentation);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                break;
            }

        }

    }

    private void importComposition() throws IOException {
        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_COMPO_bdpm.txt");

        logger.info("Import compositions");

        int colCodeCis = 0;
        int colLibelleSubstance = 3;
        int colCodeSubstance = 2;
        int colDosageSubstance = 4;
        int colNature = 6;

        String naturePrincipeActif = "SA";
        String natureExcipient = "ST";

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;

        while ((line = reader.readLine()) != null) {
            counter++;
            String[] columns = line.split("\t");

            Medicament med = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis])).orElse(null);
            Long substanceId = Long.parseLong(columns[colCodeSubstance]);

            if (med == null) continue;

            if (counter % 1000 == 0) logger.info((counter * 100 / 32000) + "%");

            if (columns[colNature].equals(naturePrincipeActif)) {
                GroupeMedicament groupeMedicament = med.getGroupeMedicament();

                GroupeMedicamentPrincipeActifKey id = new GroupeMedicamentPrincipeActifKey(groupeMedicament.getId(), substanceId);
                if (!groupeMedicamentsPrincipeActifRepository.existsById(id)) {
                    PrincipeActif principeActif = new PrincipeActif();
                    principeActif.setId(Long.valueOf(columns[colCodeSubstance]));
                    principeActif.setLibelle(columns[colLibelleSubstance]);
                    principesActifsRepository.save(principeActif);

                    GroupeMedicamentPrincipeActif asso = new GroupeMedicamentPrincipeActif();
                    asso.setId(id);
                    asso.setDosage(columns[colDosageSubstance]);
                    groupeMedicamentsPrincipeActifRepository.save(asso);
                }
            } else {
                MedicamentExcipientKey id = new MedicamentExcipientKey(med.getCodeCIS(), substanceId);

                if (!medicamentsExcipientsRepository.existsById(id)) {
                    Excipient excipient = new Excipient();
                    excipient.setId(Long.valueOf(columns[colCodeSubstance]));
                    excipient.setLibelle(columns[colLibelleSubstance]);
                    excipientsRepository.save(excipient);

                    MedicamentExcipient asso = new MedicamentExcipient();
                    asso.setId(id);
                    asso.setDosage(columns[colDosageSubstance]);
                    medicamentsExcipientsRepository.save(asso);
                }
            }

        }

        logger.info("Import end");

    }

    private void importMedicament() throws IOException {
        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_bdpm.csv");

        logger.info("Import medoc");

        int colCodeCis = 0;
        int colLibelle = 1;
        int colFormePharmaceutique = 2;
        int colTitulaires = 10;
        int colSurveillanceRenforcee = 11;
        int colVoiesAdmin = 3;

        InputStreamReader streamReader =
                new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            counter++;
            if (counter % 1000 == 0) logger.info((counter * 100 / 15000) + "%");

            String[] columns = line.split("\t");

            String[] titulaires = columns[colTitulaires].split(";");
            List<Fabricant> fabricants = new ArrayList<>();
            for (String titulaire : titulaires) {
                if (!fabricantsRepository.existsByLibelle(titulaire)) {
                    Fabricant fabricant = new Fabricant();
                    fabricant.setLibelle(titulaire.trim());
                    fabricantsRepository.save(fabricant);
                }

                fabricants.add(fabricantsRepository.getByLibelle(titulaire));
            }
            String[] voiesAdministration = columns[colVoiesAdmin].split(";");

            List<TypeAdministration> voies = new ArrayList<>();
            for (String voieAdministration : voiesAdministration) {
                if (!typesAdministrationRepository.existsByLibelle(voieAdministration)) {
                    TypeAdministration typeAdministration = new TypeAdministration();
                    typeAdministration.setLibelle(voieAdministration);
                    typesAdministrationRepository.save(typeAdministration);
                }

                voies.add(typesAdministrationRepository.getByLibelle(voieAdministration));
            }


            if (!medicamentsRepository.existsById(Long.parseLong(columns[colCodeCis]))) {
                Medicament medicament = new Medicament();
                medicament.setCodeCIS(Long.parseLong(columns[colCodeCis]));
                medicament.setLibelle(columns[colLibelle]);
                medicament.setFormePharmaceutique(columns[colFormePharmaceutique]);
                medicament.setFabricants(fabricants);
                medicament.setASurveillanceRenforce(columns[colSurveillanceRenforcee].equals("Oui"));
                medicament.setTypesAdministration(voies);
                medicamentsRepository.save(medicament);
            }
        }

    }

    public void importGroupeMedicament() throws IOException {
        logger.info("Import groupe medicament");

        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_GENER_bdpm.csv");

        InputStreamReader streamReader =
                new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        int colIdGroupeMedicament = 0;
        int colLabelGroupeMedicament = 1;
        int colCodeCis = 2;
        int colTypeMedicament = 3;

        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            counter++;
            if (counter % 1000 == 0) logger.info((counter * 100 / 15000) + "%");

            String[] columns = line.split("\t");
            Long idGroupeMedicament = Long.parseLong(columns[colIdGroupeMedicament]);

            if (!groupeMedicamentsRepository.existsById(idGroupeMedicament)) {
                GroupeMedicament groupeMedicament = new GroupeMedicament();
                groupeMedicament.setLibelle(columns[colLabelGroupeMedicament]);
                groupeMedicamentsRepository.save(groupeMedicament);
            }

            Optional<Medicament> medicament = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis]));
            if (medicament.isPresent()) {
                Medicament med = medicament.get();
                med.setGroupeMedicament(groupeMedicamentsRepository.findById(idGroupeMedicament).get());
                med.setEstReference(columns[colTypeMedicament].equals("0"));
                medicamentsRepository.save(med);
            }
        }

        medicamentsRepository.findByGroupeMedicamentIsNull().forEach((med) -> {
            med.setEstReference(true);
            GroupeMedicament groupeMedicament = new GroupeMedicament();
            groupeMedicament.setLibelle(med.getLibelle());
            med.setGroupeMedicament(groupeMedicament);
            groupeMedicamentsRepository.save(groupeMedicament);
            medicamentsRepository.save(med);
        });
    }
}
