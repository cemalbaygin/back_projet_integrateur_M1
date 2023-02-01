package fr.uga.miage.m1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.miage.m1.config.JwtAuthenticationFilter;
import fr.uga.miage.m1.config.SecurityConfiguration;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.CommandeTypeDTO;
import fr.uga.miage.m1.model.dto.PresentationForCommandeTypeDTO;
import fr.uga.miage.m1.service.CommandeTypeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommandeTypeController.class, excludeAutoConfiguration = SecurityConfiguration.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandeTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandeTypeService commandeTypeService;
    @Mock
    private Authentication authentication;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private List<CommandeTypeDTO> commandeTypeDTOS;
    private List<PresentationForCommandeTypeDTO> presentationsForCommandeTypeDTO;
    private CommandeTypeDTO commandeTypeDTO;
    private PresentationForCommandeTypeDTO presentationForCommandeTypeDTO;
    @BeforeAll
    void before() {
        when(authentication.getPrincipal()).thenReturn(new Utilisateur());

        presentationForCommandeTypeDTO = PresentationForCommandeTypeDTO.builder()
                .quantite(1)
                .codeCIP13(1L)
                .libelle("presentation")
                .libelleMedicament("medoc")
                .build();

        presentationsForCommandeTypeDTO = Arrays.asList(presentationForCommandeTypeDTO);

        commandeTypeDTO = CommandeTypeDTO.builder()
                .presentations(presentationsForCommandeTypeDTO)
                .libelle("CommandeType")
                .build();

        commandeTypeDTOS = Arrays.asList(commandeTypeDTO);
    }

    @Test
    void getCommandeType_Success() throws Exception {
        when(commandeTypeService.getListCommandeType(any(Utilisateur.class))).thenReturn(commandeTypeDTOS);

        mockMvc.perform(get("/private/commandetype")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commandeTypeDTOS))
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void getCommandeType_Fail() throws Exception {
        when(commandeTypeService.getListCommandeType(any(Utilisateur.class)))
            .thenThrow(new NoSuchElementException("Aucun établissement trouvé."));

        mockMvc.perform(get("/private/commandetype")
                        .principal(authentication))
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

