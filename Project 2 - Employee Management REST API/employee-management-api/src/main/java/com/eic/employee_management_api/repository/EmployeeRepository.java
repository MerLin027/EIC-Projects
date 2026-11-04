package com.eic.employee_management_api.repository;

import com.eic.employee_management_api.entity.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long>
{
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    @Override
    @EntityGraph(attributePaths = "address")
    List <Employee> findAll();
    @Override
    @EntityGraph(attributePaths = "address")
    // Indicates -> when running this query, also load these related entities in the same query. Prevents N+1 problem
    Optional<Employee> findById(@NonNull Long id);
}
