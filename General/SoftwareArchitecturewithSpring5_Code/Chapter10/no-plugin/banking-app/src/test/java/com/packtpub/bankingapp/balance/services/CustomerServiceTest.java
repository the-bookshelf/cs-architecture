package com.packtpub.bankingapp.balance.services;

import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceTest {

    @Test
    public void theNotificationChannelsAreSavedByTheDataRepository() throws Exception {
        NotificationType channelOne = NotificationType.EMAIL;
        NotificationType channelTwo = NotificationType.FAX;
        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(customerRepository);
        List<NotificationType> notificationChannels = new ArrayList();
        notificationChannels.add(channelOne);
        notificationChannels.add(channelTwo);
        Customer customer = Mockito.mock(Customer.class);

        customerService.savePreferredNotificationChannels(customer, notificationChannels);

        Mockito.verify(customer, Mockito.times(1)).setPreferredNotificationChannels(notificationChannels);
        Mockito.verify(customerRepository, Mockito.times(1)).save(customer);
    }
}
