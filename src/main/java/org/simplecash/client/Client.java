package org.simplecash.client;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String lastName;
    @Setter
    private String firstName;
    @Setter
    private String address;
    @Setter
    private String postalCode;
    @Setter
    private String city;
    @Setter
    private String phone;

    protected Client() {
    }

    public Client(String lastName, String firstName, String address, String postalCode, String city, String phone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phone = phone;
    }

}
