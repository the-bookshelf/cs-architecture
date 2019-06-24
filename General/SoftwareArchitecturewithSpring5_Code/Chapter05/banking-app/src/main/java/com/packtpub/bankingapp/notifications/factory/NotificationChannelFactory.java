package com.packtpub.bankingapp.notifications.factory;

import com.packtpub.bankingapp.notifications.channels.NotificationChannel;
import com.packtpub.bankingapp.notifications.channels.impl.EmailNotificationChannel;
import com.packtpub.bankingapp.notifications.channels.impl.FaxNotificationChannel;
import com.packtpub.bankingapp.notifications.domain.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationChannelFactory {

    private Map<NotificationType, NotificationChannel> notificationChannels;

    @Autowired
    public NotificationChannelFactory(EmailNotificationChannel emailNotificationChannel, FaxNotificationChannel faxNotificationChannel) {
        notificationChannels =  new HashMap<>();
        notificationChannels.put(NotificationType.EMAIL, emailNotificationChannel);
        notificationChannels.put(NotificationType.FAX, faxNotificationChannel);
    }

    public NotificationChannel getNotificationChannel(NotificationType type) {
        return notificationChannels.get(type);
    }

}
