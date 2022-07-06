package com.spring.security;

import com.spring.exception.EmailNotFoundException;
import com.spring.exception.ResourceNotFoundException;
import com.spring.model.Employee;
import com.spring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private String email;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee =employeeRepository.findByEmail(username.toLowerCase()).orElseThrow(()->new EmailNotFoundException("Invalid email address"));
        email=username;
        return employee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
