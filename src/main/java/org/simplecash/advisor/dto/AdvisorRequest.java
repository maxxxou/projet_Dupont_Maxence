package org.simplecash.advisor.dto;

import jakarta.validation.constraints.NotBlank;

public record AdvisorRequest(
        @NotBlank String lastName,
        @NotBlank String firstName
) {
}
