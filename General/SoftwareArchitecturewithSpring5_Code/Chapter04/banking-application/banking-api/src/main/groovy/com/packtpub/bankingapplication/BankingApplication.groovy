package com.packtpub.bankingapplication

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan(value="com.packtpub.bankingapplication.security.filter")
class BankingApplication {

    static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}