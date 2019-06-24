package com.packtpub.bankingapp.balance.dao;

import com.packtpub.bankingapp.balance.domain.Balance;
import com.packtpub.bankingapp.balance.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Balance findByCustomer(Customer customer);
}
