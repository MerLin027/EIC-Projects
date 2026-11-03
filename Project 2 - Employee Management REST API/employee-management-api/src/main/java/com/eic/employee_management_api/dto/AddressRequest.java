package com.eic.employee_management_api.dto;

import jakarta.validation.constraints.NotBlank;

//
public record AddressRequest(String street,
                             @NotBlank(message = "City is required") String city,
                             @NotBlank(message = "State is required") String state,
                             @NotBlank(message = "Postal code is required") String postalCode,
                             @NotBlank(message = "Country is required") String country)
{
}
