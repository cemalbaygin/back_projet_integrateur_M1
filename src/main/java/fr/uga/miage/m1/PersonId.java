package fr.uga.miage.m1;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class PersonId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 370749138489851934L;

	private String firstName;

    private String lastName;

    // default constructor
    public PersonId() {
    	
    }

    public PersonId(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // equals() and hashCode()
}
