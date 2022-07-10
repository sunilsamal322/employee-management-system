package com.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Employee> employees;
    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Asset> assets;
}
