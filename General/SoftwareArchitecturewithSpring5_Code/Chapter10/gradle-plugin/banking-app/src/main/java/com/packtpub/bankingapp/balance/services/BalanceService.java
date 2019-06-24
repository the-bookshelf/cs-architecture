package com.packtpub.bankingapp.balance.services;

import com.packtpub.bankingapp.balance.dao.BalanceRepository;
import com.packtpub.bankingapp.balance.domain.Balance;
import com.packtpub.bankingapp.balance.domain.Customer;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import com.packtpub.bankingapp.notifications.factory.NotificationChannelFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceService {

    private NotificationChannelFactory notificationChannelFactory;
    private BalanceRepository balanceRepository;


    public BalanceService(NotificationChannelFactory notificationChannelFactory, BalanceRepository balanceRepository) {
        this.notificationChannelFactory = notificationChannelFactory;
        this.balanceRepository = balanceRepository;
    }

    public void sendBalance(Customer customer) {
        List<NotificationType> preferredChannels = customer.getPreferredNotificationChannels();
        Balance balance = balanceRepository.findByCustomer(customer);
        preferredChannels.forEach(
                channel ->
                        notificationChannelFactory.getNotificationChannel(channel).send(balance)
        );
    }
}
