package com.spring.controller;

import com.spring.dto.ApiResponse;
import com.spring.dto.OrganizationDto;
import com.spring.service.OrganizationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationServices organizationServices;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<OrganizationDto> saveOrganization(@Valid @RequestBody OrganizationDto organizationDto)
    {
        return new ResponseEntity<>(organizationServices.saveOrganization(organizationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(organizationServices.getOrganizationById(id),HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations()
    {
        return new ResponseEntity<>(organizationServices.getAllOrganizations(),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@Valid @RequestBody OrganizationDto organizationDto, @PathVariable Integer id)
    {
        return new ResponseEntity<>(organizationServices.updateOrganization(id,organizationDto),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrganization(@PathVariable Integer id)
    {
        organizationServices.deleteOrganization(id);
        return new ResponseEntity<>(new ApiResponse("Organization deleted successfully",String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
}
