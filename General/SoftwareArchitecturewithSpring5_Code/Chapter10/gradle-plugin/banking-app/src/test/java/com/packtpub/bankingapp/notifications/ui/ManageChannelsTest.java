package com.packtpub.bankingapp.notifications.ui;


import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManageChannelsTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    ManageChannels manageChannels;

    @Test
    public void theUserNotificationChannelsAreAddedAsPartOfTheModel() throws Exception {
        Model model = mock(Model.class);
        String username = "foo";
        List<NotificationType> preferredChannels = new ArrayList<>();
        Customer customer = new Customer();
        customer.setPreferredNotificationChannels(preferredChannels);
        when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));
        User loggedInUser = mock(User.class);
        when(loggedInUser.getUsername()).thenReturn(username);


        manageChannels.manageNotificationChannels(model, loggedInUser);

        verify(model, times(1)).addAttribute("preferredChannels", preferredChannels);
    }
}
