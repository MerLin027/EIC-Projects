package com.eic.employee_management_api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeRequest(@NotBlank String firstName,
                              @NotBlank String lastName,
                              @NotBlank @Email String email,
                              @Pattern(regexp = "[0-9]{10}", message = "Salary must always be +ve") String phone,
                              String department,
                              @NotNull @Positive Double salary,
                              LocalDate joiningDate)
{
}