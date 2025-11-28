package org.simplecash.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank String lastName,
        @NotBlank String firstName,
        @NotBlank String address,
        @NotBlank @Size(min = 5, max = 5) String postalCode,
        @NotBlank String city,
        String phone
) {
}
