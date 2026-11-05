package com.eic.employee_management_api.entity;

import com.eic.employee_management_api.enumeration.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Setter
@Getter
@NoArgsConstructor
public class Employee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(length = 10)
    private String phone;
    private String department;
    @Column(nullable = false)
    private Double salary;
    private LocalDate joiningDate;
    @OneToOne(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id") // names the Foreign Key column that gets added to the employees table
    private Address address;
}
