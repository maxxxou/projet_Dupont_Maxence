package org.simplecash.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    long countByAdvisorId(Long advisorId);

    long countByAgencyId(Long agencyId);
}
