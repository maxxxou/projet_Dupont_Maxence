package org.simplecash.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.simplecash.client.ClientType;

public record ClientRequest(
        @NotBlank
        String lastName,
        @NotBlank
        String firstName,
        @NotBlank
        String address,
        @NotBlank
        String postalCode,
        @NotBlank
        String city,
        @NotBlank
        String phone,
        @NotNull
        Long agencyId,
        Long advisorId,
        ClientType clientType
) {
}
