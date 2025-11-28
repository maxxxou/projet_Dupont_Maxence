package org.simplecash.advisor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    long countByAgencyId(Long agencyId);
}
