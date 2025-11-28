package org.simplecash.client;

import jakarta.persistence.*;
import org.simplecash.advisor.Advisor;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;
    private String address;
    private String postalCode;
    private String city;
    private String phone;

    @Enumerated(EnumType.STRING)
    private ClientType type;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    protected Client() {
    }

    public Client(String lastName, String firstName, String address, String postalCode, String city, String phone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phone = phone;
        this.type = ClientType.PERSONAL;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public ClientType getType() {
        return type;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }
}
