package com.packtpub.bankingapp.security.auth;

import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankingUsersDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Autowired
    public BankingUsersDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerFound = customerRepository.findByUsername(username);
        if (customerFound.isPresent()) {
            Customer customer = customerFound.get();
            User.UserBuilder builder = User
                    .withUsername(username)
                    .password(customer.getPassword())
                    .roles("CUSTOMER");
            return builder.build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

    }
}
