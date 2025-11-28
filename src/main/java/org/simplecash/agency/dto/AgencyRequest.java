package org.simplecash.agency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AgencyRequest(
        @NotBlank
        @Size(min = 5, max = 5)
        String code,
        LocalDate createdAt,
        @NotBlank
        String managerLastName,
        @NotBlank
        String managerFirstName
) {
}
