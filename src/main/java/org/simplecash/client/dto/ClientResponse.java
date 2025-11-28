package org.simplecash.client.dto;

public record ClientResponse(
        Long id,
        String lastName,
        String firstName,
        String address,
        String postalCode,
        String city,
        String phone,
        Long advisorId
) {
}
