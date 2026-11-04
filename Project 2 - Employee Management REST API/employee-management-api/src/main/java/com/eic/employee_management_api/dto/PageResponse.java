package com.eic.employee_management_api.dto;

import java.util.List;

public record PageResponse(List <EmployeeResponse> employees, Long page, Long size, Long totalElements, Long totalPages)
{
}
