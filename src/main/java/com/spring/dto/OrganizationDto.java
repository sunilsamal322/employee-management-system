package com.spring.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "organization")
public class OrganizationDto {

    private Integer id;
    @NotBlank
    @Size(min=5,message = "name should be length of at least 5 characters")
    private String name;
    @NotBlank
    @Size(min=2,message = "name should be length of at least 2 characters")
    private String address;
}
