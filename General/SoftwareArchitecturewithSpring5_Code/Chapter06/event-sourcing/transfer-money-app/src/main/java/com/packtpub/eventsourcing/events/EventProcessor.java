package com.packtpub.eventsourcing.events;

import com.packtpub.eventsourcing.customer.state.Account;
import com.packtpub.eventsourcing.customer.state.Customer;
import com.packtpub.eventsourcing.customer.state.persistence.AccountRepository;
import com.packtpub.eventsourcing.customer.state.persistence.CustomerRepository;
import com.packtpub.eventsourcing.events.domain.EventMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProcessor {

    public void process(EventMetadata event) {
        if ("CustomerCreated".equals(event.getEventName())) {
            Customer customer = new Customer(event);
            customerRepository.save(customer);
        } else if ("AccountCreated".equals(event.getEventName())) {
            Account account = new Account(event);
            accountRepository.save(account);
        }
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;
}
