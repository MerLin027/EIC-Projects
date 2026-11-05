package com.eic.employee_management_api.dto;

import com.eic.employee_management_api.enumeration.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record EmployeeResponse(Long id,
                               EmployeeStatus status,
                               String fullName,
                               String email,
                               String phone,
                               String department,
                               Double salary,
                               LocalDate joiningDate,
                               AddressResponse address)
{
}
