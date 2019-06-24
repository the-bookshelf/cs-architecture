package com.packtpub.demohateoas.countries.domain;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class Customer extends ResourceSupport {

    private String name;
    private Integer customerId;

    public Customer(Integer customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }
}
