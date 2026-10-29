package com.eic.employee_management_api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) @JsonProperty("First Name")
    private String firstName;
    @Column(nullable = false) @JsonProperty("Last Name")
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(length = 10)
    private String phone;
    private String department;
    @Column(nullable = false)
    private Double salary;
    private LocalDate joiningDate;
}
