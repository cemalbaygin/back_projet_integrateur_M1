package fr.uga.miage.m1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PersonIdTest {

	@Test
	void test() {
		PersonId pi = new PersonId("Christophe", "Saint-Marcel");
		assertEquals("Christophe", pi.getFirstName());
		assertEquals("Saint-Marcel", pi.getLastName());
	}

}
