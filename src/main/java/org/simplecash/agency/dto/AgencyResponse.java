package org.simplecash.agency.dto;

import java.time.LocalDate;

public record AgencyResponse(
        Long id,
        String code,
        LocalDate createdAt,
        Long managerId,
        String managerLastName,
        String managerFirstName
) {
}
