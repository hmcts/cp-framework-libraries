package uk.gov.justice.services.yaml.subscriptiondescriptor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class SubscriptionDescriptorDef {

    @JsonProperty("subscriptions_descriptor")
    private final SubscriptionsDescriptor subscriptionsDescriptor;

    @JsonCreator
    public SubscriptionDescriptorDef(@JsonProperty("subscriptions-descriptor") final SubscriptionsDescriptor subscriptionsDescriptor) {
        this.subscriptionsDescriptor = subscriptionsDescriptor;
    }

    public SubscriptionsDescriptor getSubscriptionsDescriptor() {
        return subscriptionsDescriptor;
    }

    @Override
    public String toString() {
        return "SubscriptionDescriptorDef{" +
               "subscriptionsDescriptor=" + subscriptionsDescriptor +
               '}';
    }
}

