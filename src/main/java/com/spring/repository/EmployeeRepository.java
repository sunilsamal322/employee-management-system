package com.spring.repository;

import com.spring.model.Employee;
import com.spring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByRole(Role role);
}
