package com.packtpub.bankingapp.security.auth;

import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BankingUserDetailServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    BankingUsersDetailService bankingUsersDetailService;

    @Test(expected = UsernameNotFoundException.class)
    public void whenTheUserIsNotFoundAnExceptionIsExpected() throws Exception {
        String username = "foo";
        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

        bankingUsersDetailService.loadUserByUsername(username);
    }

    @Test
    public void theUserDetailsContainsTheInformationFromTheFoundCustomer() throws Exception {
        String username = "foo";
        String password = "bar";
        Customer customer = new Customer(username, password, NotificationType.EMAIL);
        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

        UserDetails userDetails = bankingUsersDetailService.loadUserByUsername(username);

        Assert.assertEquals(userDetails.getUsername(), username);
        Assert.assertEquals(userDetails.getPassword(), password);
        Assert.assertEquals(userDetails.getAuthorities().iterator().next().getAuthority(), "ROLE_CUSTOMER");
    }
}
