package com.eic.employee_management_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageResponse(@JsonProperty("content") List <EmployeeResponse> employees,
                           Long page,
                           Long size,
                           Long totalElements,
                           Long totalPages)
{
}
