package com.eic.employee_management_api.controller;

import com.eic.employee_management_api.dto.EmployeeRequest;
import com.eic.employee_management_api.dto.EmployeeResponse;
import com.eic.employee_management_api.dto.PageResponse;
import com.eic.employee_management_api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees")
public class EmployeeController
{
    private final EmployeeService service;
    // POST /employees
    @PostMapping
    @Operation(summary = "Creates employees")
    public ResponseEntity <EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employee)
    {
        EmployeeResponse savedEmployee = service.create(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    // GET /employees
    @GetMapping
    @Operation(summary = "Retrieves all employees")
    public ResponseEntity <PageResponse> retrieveAllEmployees(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Long page,
                                                              @RequestParam(required = false, defaultValue = "2") @Positive Long size)
    {
        return ResponseEntity.ok().body(service.findAll(page, size));
    }
    // GET /employees/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Retrieves an employee using their ID")
    public  ResponseEntity <EmployeeResponse> retrieveEmployeeById(@PathVariable Long id)
    {
        EmployeeResponse employee = service.findById(id);
        return ResponseEntity.ok(employee);
    }
    // DELETE /employees/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an employee using their ID")
    public ResponseEntity <Void> deleteEmployeeById(@PathVariable Long id)
    {
        service.deleteById(id);
        return  ResponseEntity.noContent().build();
    }
    // PUT /employees/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Updates an employee using their ID")
    public ResponseEntity <EmployeeResponse> updateEmployeeById(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employee)
    {
        EmployeeResponse updatedEmployee = service.updateById(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }
}
