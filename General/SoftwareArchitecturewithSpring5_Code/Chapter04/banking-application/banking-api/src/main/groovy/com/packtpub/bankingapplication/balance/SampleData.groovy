package com.packtpub.bankingapplication.balance

import com.packtpub.bankingapplication.balance.domain.Customer
import com.packtpub.bankingapplication.balance.persistence.CustomerRepository
import com.packtpub.bankingapplication.balance.domain.Balance
import com.packtpub.bankingapplication.balance.persistence.BalanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class SampleData implements CommandLineRunner {

    private final CustomerRepository customerRepository
    private final BalanceRepository balanceRepository

    @Autowired
    SampleData(CustomerRepository customerRepository, BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository
        this.customerRepository = customerRepository
    }

    @Override
    void run(String... args) throws Exception {
        def rene = customerRepository.save(new Customer(username: "rene", password: "rene"))
        def john = customerRepository.save(new Customer(username: "john", password: "john"))
        balanceRepository.save(new Balance(customer: rene, balance: 999))
        balanceRepository.save(new Balance(customer: john, balance: 777))
    }
}