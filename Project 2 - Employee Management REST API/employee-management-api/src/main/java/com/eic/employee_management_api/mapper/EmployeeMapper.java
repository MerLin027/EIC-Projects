package com.eic.employee_management_api.mapper;

import com.eic.employee_management_api.dto.*;
import com.eic.employee_management_api.entity.Address;
import com.eic.employee_management_api.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.PageRequest;

@Mapper(componentModel = "spring")
public interface EmployeeMapper
{
    // Converts EmployeeRequest to Employee
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Employee toEntity(EmployeeRequest request);
    // Converts Employee to EmployeeResponse
    @Mapping(source = "employee", target = "fullName", qualifiedByName = "toFullName")
    EmployeeResponse toResponse(Employee employee);
    // Updates existing Employee through EmployeeRequest
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Employee updateEntity(EmployeeRequest request, @MappingTarget Employee employee);
    //
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequest request);
    //
    AddressResponse toResponse(Address address);
    //
    @Mapping(target = "id", ignore = true)
    void updateEntity(AddressRequest request, @MappingTarget Address address);
    //
    @Named("toFullName")
    default String toFullName(Employee employee)
    {
        if(employee == null)
            return null;
        return(employee.getFirstName().trim()+" "+employee.getLastName().trim());
    }
}
