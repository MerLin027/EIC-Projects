package com.eic.employee_management_api.dto;

public record AddressResponse(Long id, String street, String city, String state,
                              String postalCode, String country)
{
}
