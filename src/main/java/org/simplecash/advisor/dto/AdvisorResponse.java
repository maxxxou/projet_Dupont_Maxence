package org.simplecash.advisor.dto;

public record AdvisorResponse(
        Long id,
        String lastName,
        String firstName,
        Long agencyId
) {
}
