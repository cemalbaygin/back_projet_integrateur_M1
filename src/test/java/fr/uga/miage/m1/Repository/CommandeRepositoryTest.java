package fr.uga.miage.m1.Repository;

import fr.uga.miage.m1.GroMedApplication;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GroMedApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
@RequiredArgsConstructor
public class CommandeRepositoryTest {


   //private final TestEntityManager entityManager;
    @Autowired
    private PresentationsRepository presentationsRepository;

    @Test
    public void  findAllIsEmpty(){
         List<Presentation> presentations = presentationsRepository.findAll();
        Assertions.assertEquals(0, presentations.size());
        assertThat(presentations).isNotNull();
    }

}
