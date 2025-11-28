package org.simplecash.advisor;

import jakarta.persistence.*;
import org.simplecash.agency.Agency;

@Entity
@Table(name = "advisors")
public class Advisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    protected Advisor() {
    }

    public Advisor(String lastName, String firstName, Agency agency) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.agency = agency;
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

    public Agency getAgency() {
        return agency;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }
}
