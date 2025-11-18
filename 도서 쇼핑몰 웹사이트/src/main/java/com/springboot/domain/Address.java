package com.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String country;
    private String zipcode;
    private String addressname;
    private String detailname;
}
