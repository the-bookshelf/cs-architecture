package com.packtpub.eventsourcing.customer.state;

import com.packtpub.eventsourcing.events.domain.EventMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Account {

    @Id
    private String accountId;

    private String customerId;

    private Integer balance;

    private String type;

    public Account(EventMetadata accountCreatedEvent) {
        JSONObject jsonObject = accountCreatedEvent.getEventData();
        this.accountId = jsonObject.get("account_id").toString();
        this.customerId = jsonObject.get("customer_id").toString();
        this.balance = Integer.valueOf(jsonObject.get("balance").toString());
        this.type = jsonObject.get("account_type").toString();
    }

}
