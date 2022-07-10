package com.spring.controller;

import com.spring.dto.ApiResponse;
import com.spring.dto.OrganizationDto;
import com.spring.model.Employee;
import com.spring.repository.OrganizationRepository;
import com.spring.service.OrganizationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationServices organizationServices;
    @Autowired
    private OrganizationRepository organizationRepository;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> saveOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        if (organizationRepository.findAll().isEmpty()) {
            return new ResponseEntity<>(organizationServices.saveOrganization(organizationDto, getLoginUser().getId()), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("You can't create more than one organization",String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(organizationServices.getOrganizationById(id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@Valid @RequestBody OrganizationDto organizationDto, @PathVariable Integer id)
    {
        return new ResponseEntity<>(organizationServices.updateOrganization(id,organizationDto),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrganization(@PathVariable Integer id)
    {
        organizationServices.deleteOrganization(id);
        return new ResponseEntity<>(new ApiResponse("Organization deleted successfully",String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
    public Employee getLoginUser()
    {
        Employee employee= (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employee;
    }
}
