package fr.uga.miage.m1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table
@Getter
@IdClass(PersonId.class)
public final class Person {
	
	public Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
    @Id
    @Column(name="first_name")
    private String firstName;
    @Id
    @Column(name="last_name")
    private String lastName;
}
