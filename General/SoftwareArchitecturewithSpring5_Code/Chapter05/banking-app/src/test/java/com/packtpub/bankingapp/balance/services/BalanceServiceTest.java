package com.packtpub.bankingapp.balance.services;


import com.packtpub.bankingapp.balance.dao.BalanceRepository;
import com.packtpub.bankingapp.balance.domain.Balance;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.channels.NotificationChannel;
import com.packtpub.bankingapp.notifications.channels.impl.EmailNotificationChannel;
import com.packtpub.bankingapp.notifications.channels.impl.FaxNotificationChannel;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import com.packtpub.bankingapp.notifications.factory.NotificationChannelFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BalanceServiceTest {

    NotificationChannel emailChannel, faxChannel;
    NotificationChannelFactory notificationChannelsFactory;

    @Before
    public void initialize() {
        emailChannel = mock(EmailNotificationChannel.class);
        faxChannel = mock(FaxNotificationChannel.class);
        notificationChannelsFactory = buildNotificationChannelFactory(emailChannel, faxChannel);
    }

    @Test
    public void theBalanceIsSendUsingThePreferredNotificationChannels() throws Exception {

        Balance balance = mock(Balance.class);
        Customer customer = mock(Customer.class);
        when(customer.getPreferredNotificationChannels()).thenReturn(getListOfNotificationTypes());
        BalanceRepository balanceRepository = mock(BalanceRepository.class);
        when(balanceRepository.findByCustomer(customer)).thenReturn(balance);
        BalanceService balanceService = new BalanceService(notificationChannelsFactory, balanceRepository);

        balanceService.sendBalance(customer);

        verify(emailChannel, times(1)).send(balance);
        verify(faxChannel, times(1)).send(balance);

    }

    private NotificationChannelFactory buildNotificationChannelFactory(NotificationChannel emailChannel, NotificationChannel faxChannel) {
        NotificationChannelFactory notificationChannelsFactory = mock(NotificationChannelFactory.class);
        when(notificationChannelsFactory.getNotificationChannel(NotificationType.EMAIL)).thenReturn(emailChannel);
        when(notificationChannelsFactory.getNotificationChannel(NotificationType.FAX)).thenReturn(faxChannel);
        return notificationChannelsFactory;
    }

    private List<NotificationType> getListOfNotificationTypes() {
        List<NotificationType> preferredNotificationChannels = new ArrayList<>();
        preferredNotificationChannels.add(NotificationType.EMAIL);
        preferredNotificationChannels.add(NotificationType.FAX);
        return preferredNotificationChannels;
    }
}
