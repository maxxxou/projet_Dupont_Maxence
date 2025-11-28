package org.simplecash.client.dto;

import org.simplecash.client.ClientType;

public record ClientResponse(
        Long id,
        String lastName,
        String firstName,
        String address,
        String postalCode,
        String city,
        String phone,
        Long agencyId,
        Long advisorId,
        ClientType clientType
) {
}
