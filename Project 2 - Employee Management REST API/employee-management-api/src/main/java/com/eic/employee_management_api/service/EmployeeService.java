package com.eic.employee_management_api.service;

import com.eic.employee_management_api.dto.EmployeeRequest;
import com.eic.employee_management_api.dto.EmployeeResponse;
import com.eic.employee_management_api.entity.Employee;
import com.eic.employee_management_api.exception.DuplicateResourceException;
import com.eic.employee_management_api.exception.ResourceNotFoundException;
import com.eic.employee_management_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EmployeeService
{
    private final EmployeeRepository repository;
    //
    private final Function <EmployeeRequest, Employee> requestToEntity = (EmployeeRequest request) ->
    {
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());
        employee.setJoiningDate(request.joiningDate());
        return employee;
    };
    private final Function <Employee, EmployeeResponse> entityToResponse = (Employee employee) ->
            new EmployeeResponse(employee.getId(), employee.getFirstName(), employee.getLastName(),
                    employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getSalary(),
                    employee.getJoiningDate());
    //
    public EmployeeResponse create(EmployeeRequest employeeRequest) throws DuplicateResourceException
    {
        Employee employee = requestToEntity.apply(employeeRequest);
        if(repository.existsByEmail(employee.getEmail()))
            throw new DuplicateResourceException("Employee with email " + employee.getEmail() + " already exists");
        return entityToResponse.apply(repository.save(employee));
    }
    public Page <EmployeeResponse> findAll(Pageable pageable)
    {
        return repository.findAll(pageable).map(entityToResponse);
    }
    public EmployeeResponse findById(Long id) throws ResourceNotFoundException
    {
        Employee employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
        return entityToResponse.apply(employee);
    }
    public void deleteById(Long id) throws ResourceNotFoundException
    {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        repository.deleteById(id);
    }
    public EmployeeResponse updateById(Long id, EmployeeRequest employeeRequest) throws ResourceNotFoundException, DuplicateResourceException
    {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        Employee updatedEmployee = requestToEntity.apply(employeeRequest);
        updatedEmployee.setId(id);
        if(repository.existsByEmailAndIdNot(updatedEmployee.getEmail(), id))
            throw new DuplicateResourceException("Employee with email " + updatedEmployee.getEmail() + " already exists");
        return entityToResponse.apply(repository.save(updatedEmployee));
    }
}
