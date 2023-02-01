//package fr.uga.miage.m1.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fr.uga.miage.m1.config.JwtAuthenticationFilter;
//import fr.uga.miage.m1.config.SecurityConfiguration;
//import fr.uga.miage.m1.entity.Utilisateur;
//import fr.uga.miage.m1.model.dto.*;
//import fr.uga.miage.m1.model.request.AjouterAuPanierDTO;
//import fr.uga.miage.m1.service.PanierService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.http.RequestEntity.post;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = PanierController.class, excludeAutoConfiguration = SecurityConfiguration.class)
//@WithMockUser
//@AutoConfigureMockMvc(addFilters = false)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class PanierControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//    @MockBean
//    private PanierController controller;
//
//    @MockBean
//    private PanierService panierService;
//
//    @Mock
//    private Authentication authentication;
//    @Mock
//    private Utilisateur utilisateur;
//
//    private PresentationMedicamentDTO presentationMedicamentDTO;
//    private AjouterAuPanierDTO ajouterAuPanierDTO;
//    private PanierPresentationDTO panierPresentationDTO;
//    private PresentationDTO presentationDto;
//    private MedicamentDTO medicamentDto;
//
//    @BeforeAll
//    void before(){
//        presentationDto = new PresentationDTO();
//        presentationDto.setCodeCIP13(1234443L);
//        presentationDto.setLibelle("plaquette(s) aluminium de 28 comprimé(s)");
//        presentationDto.setPrix(13d);
//        presentationDto.setTauxRemboursement(65);
//        presentationDto.setQuantiteStock(34);
//
//        medicamentDto = new MedicamentDTO();
//        medicamentDto.setLibelle("ACICLOVIR EG 800 mg, comprimé");
//        medicamentDto.setFabricants(null);
//        medicamentDto.setCodeCIS(64776881l);
//        medicamentDto.setEstReference(true);
//        medicamentDto.setFormePharmaceutique("comprimé");
//        medicamentDto.setASurveillanceRenforce(true);
//
//        presentationMedicamentDTO = new PresentationMedicamentDTO();
//        presentationMedicamentDTO.setPresentation(presentationDto);
//        presentationMedicamentDTO.setMedicament(medicamentDto);
//
//        ajouterAuPanierDTO = AjouterAuPanierDTO
//                .builder()
//                .quantite(1)
//                .code_CIP13("codeCIP13")
//                .build();
//
//        panierPresentationDTO = new PanierPresentationDTO();
//        panierPresentationDTO.setMedicament(medicamentDto);
//        panierPresentationDTO.setPresentation(presentationDto);
//        panierPresentationDTO.setQuantite(2);
//
//        utilisateur = new Utilisateur();
//        utilisateur.setId(1);
//        utilisateur.setEmail("a@gmail.com");
//        utilisateur.setPassword("password");
//
//        when(authentication.getPrincipal()).thenReturn(utilisateur);
//    }
//
//    @Test
//    public void getPanier() {
//        List<PanierPresentationDTO> expected = Arrays.asList(new PanierPresentationDTO(), new PanierPresentationDTO());
//        when(panierService.getPanier(utilisateur)).thenReturn(expected);
//
//        List<PanierPresentationDTO> result = controller.getPanier(authentication).getBody();
//
//        assertEquals(expected, result);
//        verify(panierService, times(1)).getPanier(utilisateur);
//    }
//
//    @Test
//    public void addToPanier() {
//        when(panierService.addPresentationToPanier(utilisateur, ajouterAuPanierDTO)).thenReturn(true);
//        boolean result = controller.addToPanier(authentication, ajouterAuPanierDTO).getBody();
//        assertEquals(true, result);
//        verify(panierService, times(1)).addPresentationToPanier(utilisateur, ajouterAuPanierDTO);
//    }
//
//    @Test
//    public void deleteFromPanierTest() throws Exception {
//        List<PanierPresentationDTO> panier = new ArrayList<>();
//        panier.add(panierPresentationDTO);
//        when(panierService.deleteFromPanier(utilisateur, "codeCIP13")).thenReturn(panier);
//
//        mockMvc.perform(delete("/private/panier")
//                        .param("code_CIP13", "codeCIP13")
//                        .content(asJsonString(panier))
//                        .principal(authentication))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void updateFromPanierTest() throws Exception {
//        List<PanierPresentationDTO> panier = new ArrayList<>();
//        panier.add(panierPresentationDTO);
//
//        when(panierService.updateFromPanier(utilisateur, ajouterAuPanierDTO)).thenReturn(panier);
//
//        mockMvc.perform(put("/private/panier")
//                        .principal(authentication)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(ajouterAuPanierDTO)))
//                .andExpect(status().isOk());
//    }
//
////    @Test
////    public void substituerProduitTest() throws Exception {
////        List<PresentationMedicamentDTO> medicaments = new ArrayList<>();
////        medicaments.add(presentationMedicamentDTO);
////
////        when(panierService.getSimilaires("codeCIP13")).thenReturn(medicaments);
////
////        mockMvc.perform(post("/private/panier/codeCIP13")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(asJsonString(medicaments)))
////                .andExpect(status().isOk());
////    }
//
////    @Test
////    public void substituerProduitAuPanierTest() throws Exception {
////        when(panierService.substituerProduit(utilisateur, "sourceCodeCIP13", "destinationCodeCIP13")).thenReturn(true);
////
////        mockMvc.perform(post("/private/panier/sourceCodeCIP13/substituer")
////                        .param("destination", "destinationCodeCIP13"))
////                .andExpect(status().isOk())
////                .principal(authentication)
////                .andExpect(content().string("true"));
////    }
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}