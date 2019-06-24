package com.packtpub.demohateoas.countries.domain;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class BankStatement extends ResourceSupport {

    private Integer bankStatementId;
    private String information;

    public BankStatement(Integer bankStatementId, String information) {
        this.bankStatementId = bankStatementId;
        this.information = information;
    }
}
