package fr.uga.miage.m1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MyApplicationTest {

	@InjectMocks
	MyApplication myApp;

	@Mock
	PersonsRepository repository;

	@Test
	void test_with_one_person() {
		when(repository.count()).thenReturn(1L);
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("John", "LeCarre"));
		when(repository.findAll()).thenReturn(persons);
		assertEquals("\"John LeCarre\"", myApp.testDBConnexion());
	}

	@Test
	void test_without_any_person() {
		when(repository.count()).thenReturn(0L);
		assertEquals("No person found", myApp.testDBConnexion());
	}

}
