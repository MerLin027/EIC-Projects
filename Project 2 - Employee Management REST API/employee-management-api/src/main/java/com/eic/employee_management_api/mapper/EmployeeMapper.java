package com.eic.employee_management_api.mapper;

import com.eic.employee_management_api.dto.AddressRequest;
import com.eic.employee_management_api.dto.AddressResponse;
import com.eic.employee_management_api.dto.EmployeeRequest;
import com.eic.employee_management_api.dto.EmployeeResponse;
import com.eic.employee_management_api.entity.Address;
import com.eic.employee_management_api.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper
{
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequest request);
    // Converts EmployeeRequest to Employee
    EmployeeResponse toResponse(Employee employee);
    // Converts Employee to EmployeeResponse
    @Mapping(target = "id", ignore = true)
    Employee updateEntity(EmployeeRequest request, @MappingTarget Employee employee);
    // Updates existing Employee through EmployeeRequest
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequest request);
    //
    AddressResponse toResponse(Address address);
    //
    @Mapping(target = "id", ignore = true)
    void updateEntity(AddressRequest request, @MappingTarget Address address);
}
