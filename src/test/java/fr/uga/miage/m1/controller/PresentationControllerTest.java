package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.config.JwtAuthenticationFilter;
import fr.uga.miage.m1.config.SecurityConfiguration;
import fr.uga.miage.m1.model.dto.*;
import fr.uga.miage.m1.service.PresentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PresentationController.class, excludeAutoConfiguration = SecurityConfiguration.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
public class PresentationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PresentationService presentationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private PresentationMedicamentDTO dto1;
    private PresentationMedicamentDTO dto2;
    private PresentationMedicamentDTO dto3;

    private PresentationDTO presentationDto1;
    private PresentationDTO presentationDto2;
    private PresentationDTO presentationDto3;

    private MedicamentDTO medicamentDto1;
    private MedicamentDTO medicamentDto2;
    private MedicamentDTO medicamentDto3;

    private Page<PresentationMedicamentDTO> presentationDtos;

    private static final String PRESENTATION_PATH = "/public/presentations";



    @BeforeEach
    void before(){

        this.presentationDto1 = new PresentationDTO();
        presentationDto1.setCodeCIP13(1234443L);
        presentationDto1.setLibelle("plaquette(s) aluminium de 28 comprimé(s)");
        presentationDto1.setPrix(13d);
        presentationDto1.setTauxRemboursement(65);
        presentationDto1.setQuantiteStock(34);

        this.presentationDto2 = new PresentationDTO();
        presentationDto2.setCodeCIP13(4534443L);
        presentationDto2.setLibelle("plaquette(s) PVC PVDC aluminium de 14 comprimé(s)");
        presentationDto2.setPrix(11d);
        presentationDto2.setTauxRemboursement(65);
        presentationDto2.setQuantiteStock(14);

        this.presentationDto3 = new PresentationDTO();
        presentationDto3.setCodeCIP13(6784443L);
        presentationDto3.setLibelle("1 plaquette(s) PVC PVDC aluminium de 1 comprimé(s)");
        presentationDto3.setPrix(17d);
        presentationDto3.setTauxRemboursement(65);
        presentationDto3.setQuantiteStock(20);

        this.medicamentDto1 = new MedicamentDTO();
        medicamentDto1.setLibelle("ACICLOVIR EG 800 mg, comprimé");
        medicamentDto1.setFabricants(null);
        medicamentDto1.setCodeCIS(64776881l);
        medicamentDto1.setEstReference(true);
        medicamentDto1.setFormePharmaceutique("comprimé");
        medicamentDto1.setASurveillanceRenforce(true);

        this.medicamentDto2 = new MedicamentDTO();
        medicamentDto2.setLibelle("ACICLOVIR HIKMA 250 mg, poudre pour solution pour perfusion");
        medicamentDto2.setFabricants(null);
        medicamentDto2.setCodeCIS(56876881l);
        medicamentDto2.setEstReference(true);
        medicamentDto2.setFormePharmaceutique("poudre pour solution pour perfusion");
        medicamentDto2.setASurveillanceRenforce(false);

        this.medicamentDto3 = new MedicamentDTO();
        medicamentDto3.setLibelle("ACICLOVIR HIKMA 500 mg, poudre pour solution pour perfusion");
        medicamentDto3.setFabricants(null);
        medicamentDto3.setCodeCIS(6857881l);
        medicamentDto3.setEstReference(false);
        medicamentDto3.setFormePharmaceutique("poudre pour solution pour perfusion");
        medicamentDto3.setASurveillanceRenforce(false);

        this.dto1 = new PresentationMedicamentDTO();
        dto1.setPresentation(presentationDto1);
        dto1.setMedicament(medicamentDto1);

        this.dto2 = new PresentationMedicamentDTO();
        dto2.setPresentation(presentationDto2);
        dto2.setMedicament(medicamentDto2);

        this.dto3 = new PresentationMedicamentDTO();
        dto3.setPresentation(presentationDto3);
        dto3.setMedicament(medicamentDto3);


    }

    /**
     * Test que la route /api/public/presentations renvoie la liste des présentations présents dans la bdd
     * @see PresentationController#index(Optional, Pageable)
     */
    @Test
    void getAllPresentations() throws Exception {
        final Pageable pageable = PageRequest.of(0,3);
        List<PresentationMedicamentDTO> list = Arrays.asList(dto1, dto2, dto3);

        presentationDtos = new PageImpl<PresentationMedicamentDTO>(list,pageable, list.size());

        when(presentationService.getPresentationsWithFilter(any(),any(), any(),any(), any())).thenReturn(presentationDtos);

        doGetPageWithParams(PRESENTATION_PATH,
                Optional.empty(),
                Optional.empty())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].presentation.codeCIP13").value(1234443L))
                .andExpect(jsonPath("$.content[1].presentation.codeCIP13").value(4534443L))
                .andExpect(jsonPath("$.content[2].presentation.codeCIP13").value(6784443L));


    }

    /**
     * Test que la route /api/public/presentations renvoie la liste des présentations présents dans la bdd
     * @see PresentationController#show(Long) 
     */
    @Test
    void getPresentationByCIP13() throws Exception {

        ExcipientDTO excipientDTO1 = new ExcipientDTO();
        excipientDTO1.setLibelle("excip1");
        excipientDTO1.setDosage("300 mg");

        ExcipientDTO excipientDTO2 = new ExcipientDTO();
        excipientDTO2.setLibelle("excip2");
        excipientDTO2.setDosage("250 mg");

        PrincipeActifDTO principeActifDTO = new PrincipeActifDTO();
        principeActifDTO.setDosage("300 mg");
        principeActifDTO.setLibelle("principe1");

        GroupeMedicamentDTO groupeMedicamentDTO = new GroupeMedicamentDTO();
        groupeMedicamentDTO.setLibelle("gpMedicament1");
        groupeMedicamentDTO.setPresentationsMedicaments(Arrays.asList(dto1, dto2, dto3));
        groupeMedicamentDTO.setPrincipesActifs(Arrays.asList(principeActifDTO));

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setLibelle("prescrip1");

        PresentationCompleteDTO presentationCompleteDTO = new PresentationCompleteDTO();
        presentationCompleteDTO.setPresentations(Arrays.asList(presentationDto1));
        presentationCompleteDTO.setMedicament(medicamentDto1);
        presentationCompleteDTO.setExcipients(Arrays.asList(excipientDTO1,excipientDTO2));
        presentationCompleteDTO.setPrescriptions(Arrays.asList(prescriptionDTO));
        presentationCompleteDTO.setGroupeMedicament(groupeMedicamentDTO);


        when(presentationService.getPresentation(1234443l)).thenReturn(presentationCompleteDTO);

        doGetPageWithParams(PRESENTATION_PATH+"/1234443",
                Optional.empty(),
                Optional.empty())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presentations").isNotEmpty())
                .andExpect(jsonPath("$.presentations[0].codeCIP13").value(1234443L))
                .andExpect(jsonPath("$.presentations[0].libelle").value("plaquette(s) aluminium de 28 comprimé(s)"))
                .andExpect(jsonPath("$.medicament.codeCIS").value(64776881l))
                .andExpect(jsonPath("$.medicament.libelle").value("ACICLOVIR EG 800 mg, comprimé"));
    }

    private ResultActions doGetPageWithParams( String path,
                                               Optional<String> recherche,
                                               Optional<Pageable> pageable) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if(recherche.isPresent()){
            params.add("search", recherche.get());
        }
        if(pageable.isPresent()){
            params.add("size",String.valueOf(pageable.get().getPageSize()));
            params.add("page",String.valueOf(pageable.get().getPageNumber()));
        }

        return mvc.perform(get(path).params(params)
                .contentType(MediaType.APPLICATION_JSON));
    }

}

