package com.eic.employee_management_api.repository;

import com.eic.employee_management_api.entity.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    // @EntityGraph(attributePaths = "address")
    Optional <Employee> findById(@NonNull Long id);

    /*
    EntityGraph is a JPA feature that lets us give a query its own fetch plan
    It's called a graph cause it has a root (entity being queried - Employee) and nodes (attributes to be loaded - Address)
    It is a convenient wrapper where we declare the annotation on a repository method and Spring Data JPA builds the query for us
    attributePaths is a list of attributes on the root entity - the field names in the root entity
    Spring Data provides this as a hint. Hibernate sees this hint and changes the SQL it generates.
    */

    /*
        // Join Fetch
        @Query(value="select e from Employee e left join fetch e.address", countQuery="select count(e) from Employee e")
        Page <Employee> findAll(Pageable pageable);
     */
}
