package com.packtpub.bankingapp.balance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Balance {

    @Id
    @GeneratedValue
    private long idBalance;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
