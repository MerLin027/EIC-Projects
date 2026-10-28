package com.eic.employee_management_api.dto;

import java.time.LocalDate;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, String phone, String department,
                               Double salary, LocalDate joiningDate)
{
}
