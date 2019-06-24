package com.packtpub.bankingapp.balance.services;

import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void savePreferredNotificationChannels(Customer customer, List<NotificationType> notificationChannels) {
        customer.setPreferredNotificationChannels(notificationChannels);
        customerRepository.save(customer);
    }
}
