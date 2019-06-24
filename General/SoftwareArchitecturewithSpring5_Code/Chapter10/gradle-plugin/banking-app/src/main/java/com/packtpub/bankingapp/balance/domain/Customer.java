package com.packtpub.bankingapp.balance.domain;

import com.packtpub.bankingapp.notifications.domain.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "id_customer")
    private Long idCustomer;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = NotificationType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "preferred_notification_channels")
    private List<NotificationType> preferredNotificationChannels;


    public Customer(String username, String password, NotificationType... preferredNotificationChannels) {
        this.username = username;
        this.password = password;
        this.preferredNotificationChannels = Arrays.asList(preferredNotificationChannels);
    }
}
