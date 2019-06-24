package com.packtpub.bankingapp.notifications.channels;

import com.packtpub.bankingapp.balance.domain.Balance;

public interface NotificationChannel {

    default void send(Balance balance) {
        System.out.println("sending balance...");
    }
}
