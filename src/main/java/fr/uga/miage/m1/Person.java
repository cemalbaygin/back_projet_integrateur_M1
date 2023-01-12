package fr.uga.miage.m1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(PersonId.class)
@NoArgsConstructor
@AllArgsConstructor
@Table
public final class Person {
	
    @Id
    @Column(name="first_name")
    private String firstName;
    @Id
    @Column(name="last_name")
    private String lastName;

    @Column(name="adressePostal")
    private String adressePostal;

    @Column(name="password")
    private String password;

}
