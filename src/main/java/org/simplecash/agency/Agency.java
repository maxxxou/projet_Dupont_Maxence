package org.simplecash.agency;

import jakarta.persistence.*;
import org.simplecash.advisor.Advisor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agencies")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5, nullable = false, unique = true)
    private String code;

    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "agency")
    private List<Advisor> advisors = new ArrayList<>();

    protected Agency() {
    }

    public Agency(String code, LocalDate createdAt, Manager manager) {
        this.code = code;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Manager getManager() {
        return manager;
    }

    public List<Advisor> getAdvisors() {
        return advisors;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
