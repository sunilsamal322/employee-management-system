package com.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Employee> employees;
    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Asset> assets;
}
