package com.eic.employee_management_api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeRequest(@NotBlank(message = "First Name is reuired")
                              String firstName,
                              @NotBlank(message = "Last Name is required")
                              String lastName,
                              @NotBlank(message = "Email is required") @Email(message = "Must be a vaid email address")
                              String email,
                              @Pattern(regexp = "[0-9]{10}", message = "Not a valid Phone Number")
                              String phone,
                              String department,
                              @NotNull(message = "Salary cannot be null") @PositiveOrZero(message = "Salary must always be positive")
                              Double salary,
                              LocalDate joiningDate)
{
}