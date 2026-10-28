package com.eic.employee_management_api.controller;

import com.eic.employee_management_api.dto.EmployeeRequest;
import com.eic.employee_management_api.dto.EmployeeResponse;
import com.eic.employee_management_api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController
{
    private final EmployeeService service;
    //
    // POST /employees
    @PostMapping
    public ResponseEntity <EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employee)
    {
        EmployeeResponse savedEmployee = service.create(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    // GET /employees
    @GetMapping
    public ResponseEntity <PagedModel <EmployeeResponse>> retrieveAllEmployees(@PageableDefault(size = 5) Pageable pageable)
    {
        // Page is returned from service.findAll(pageable) and
        // is wrapped in PagedModel which holds its stable JSON shape
        return ResponseEntity.ok(new PagedModel <> (service.findAll(pageable)));
    }
    // GET /employees/{id}
    @GetMapping("/{id}")
    public  ResponseEntity <EmployeeResponse> retrieveEmployeeById(@PathVariable Long id)
    {
        EmployeeResponse employee = service.findById(id);
        return ResponseEntity.ok(employee);
    }
    // DELETE /employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteEmployeeById(@PathVariable Long id)
    {
        service.deleteById(id);
        return  ResponseEntity.noContent().build();
    }
    // PUT /employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity <EmployeeResponse> updateEmployeeById(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employee)
    {
        EmployeeResponse updatedEmployee = service.updateById(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }
}
