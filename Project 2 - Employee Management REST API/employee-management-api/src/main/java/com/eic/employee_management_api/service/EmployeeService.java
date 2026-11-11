package com.eic.employee_management_api.service;

import com.eic.employee_management_api.dto.EmployeeRequest;
import com.eic.employee_management_api.dto.EmployeeResponse;
import com.eic.employee_management_api.entity.Employee;
import com.eic.employee_management_api.enumeration.EmployeeStatus;
import com.eic.employee_management_api.exception.DuplicateResourceException;
import com.eic.employee_management_api.exception.InvalidStatusChangeException;
import com.eic.employee_management_api.exception.ResourceNotFoundException;
import com.eic.employee_management_api.mapper.EmployeeMapper;
import com.eic.employee_management_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService
{
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    //
    public EmployeeResponse create(EmployeeRequest employeeRequest) throws DuplicateResourceException
    {
        Employee employee = mapper.toEntity(employeeRequest);
        if(repository.existsByEmail(employee.getEmail()))
            throw new DuplicateResourceException("Employee with email " + employee.getEmail() + " already exists");
        return mapper.toResponse(repository.save(employee));
    }
    @Transactional(readOnly = true) // Indicates that the method is read-only -> No data is changed
    public Page <EmployeeResponse> findAll(Pageable pageable)
    {
        return repository.findAll(pageable).map(mapper::toResponse); // employee -> mapper.toResponse(employee)
    }
    @Transactional(readOnly = true) // Indicates that the method is read-only -> No data is changed
    public EmployeeResponse findById(Long id) throws ResourceNotFoundException
    {
        Employee employee = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id " + id + " not found"));
        return mapper.toResponse(employee);
    }
    public void deleteById(Long id) throws ResourceNotFoundException
    {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        repository.deleteById(id);
    }
    @Transactional
    public EmployeeResponse updateById(Long id, EmployeeRequest employeeRequest) throws ResourceNotFoundException, DuplicateResourceException
    {
        Employee employee = repository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("Employee with id " + id + " not found"));
        if(repository.existsByEmailAndIdNot(employeeRequest.email(), id))
            throw new DuplicateResourceException("Employee with email " + employeeRequest.email() + " already exists");
        Employee updatedEmployee = mapper.updateEntity(employeeRequest, employee);
        return mapper.toResponse(repository.save(updatedEmployee));
    }
    public void updateStatus(Long id, EmployeeStatus targetStatus)
    {
        Employee employee = repository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("Employee with id " + id + " not found"));
        if(employee.getStatus().equals(targetStatus))
            throw new InvalidStatusChangeException("Employee with id " + id + " is already "+targetStatus);
       employee.setStatus(targetStatus);
       repository.save(employee);
    }
}
