package fr.uga.miage.m1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.miage.m1.config.JwtAuthenticationFilter;
import fr.uga.miage.m1.config.SecurityConfiguration;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.ProfilPostDTO;
import fr.uga.miage.m1.model.dto.UtilisateurDTO;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProfilController.class, excludeAutoConfiguration = SecurityConfiguration.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfilControllerTest {
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AutoMapper mapper;
    @Mock
    private Authentication authentication;
    private Utilisateur utilisateur;
    private UtilisateurDTO utilisateurDto;
    private ProfilPostDTO profilPostDto;
    @BeforeAll
    public void before() {
        utilisateur = new Utilisateur();
        utilisateur.setEmail("a@gmail.com");
        utilisateur.setFirstname("Yassine");
        utilisateur.setLastname("Mosleh");
        utilisateur.setPassword("password");

        utilisateurDto = UtilisateurDTO
                .builder()
                .email("a@gmail.com")
                .firstname("Yassine")
                .lastname("Mosleh")
                .build();

        profilPostDto = new ProfilPostDTO();
        profilPostDto.setEmail("a@gmail.com");
        profilPostDto.setFirstname("Yassine");
        profilPostDto.setLastname("Mosleh");
        profilPostDto.setPassword("password");
    }

    @Test
    void getProfil_Success() throws Exception {
        when(authentication.getPrincipal()).thenReturn(utilisateur);
        when(mapper.entityToDto(utilisateur)).thenReturn(utilisateurDto);

        mockMvc.perform(get("/private/profil")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void postProfil_Success() throws Exception {
        when(userService.changeMdp(authentication, profilPostDto)).thenReturn(new ResponseEntity<>(utilisateurDto, HttpStatus.OK));

        mockMvc.perform(post("/private/profil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(profilPostDto))
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}