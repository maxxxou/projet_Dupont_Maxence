package org.simplecash.advisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdvisorRequest(
        @NotBlank
        String lastName,
        @NotBlank
        String firstName,
        @NotNull
        Long agencyId
) {
}
