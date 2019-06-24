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
public class Customer {

    @Id
    private String customerId;

    private String name;

    private String lastName;

    public Customer(EventMetadata accountCreatedEvent) {
        JSONObject jsonObject = accountCreatedEvent.getEventData();
        this.name = jsonObject.get("name").toString();
        this.lastName = jsonObject.get("last_name").toString();
        this.customerId = jsonObject.get("customer_id").toString();
    }

}
