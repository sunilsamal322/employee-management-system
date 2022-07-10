package com.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "employee")
public class EmployeeDto {

    private Integer id;
    @NotBlank
    @Size(min = 2,max=10,message = "first name must be length of 2-10 characters")
    private String firstName;
    @NotBlank
    @Size(min = 2,max=10,message = "last name must be length of 2-10 characters")
    private String lastName;
    @NotBlank
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!&#%]).{6,15}$",message = "password should must must contain digit,lower case alphabet,uppercase alphabet and at least one special character(@,$,!,&), and password length should be 6-15 characters")
    private String password;
    @NotBlank
    @Pattern(regexp = ("^$|[0-9]{10}"),message = "Invalid phone number")
    private String phoneNumber;
    @NotBlank
    private String designation;
    @NotNull
    private Integer salary;
    private RoleDto role;
    private OrganizationDto organization;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String superAdminCode;
}