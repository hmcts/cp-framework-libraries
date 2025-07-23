package uk.gov.justice.services.yaml.subscriptiondescriptor;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscription {

    private final String name;
    private final List<Event> events;
    private final String eventSourceName;
    private final String prioritisation;

    @JsonCreator
    public Subscription(@JsonProperty("name") final String name,
                        @JsonProperty("events") final List<Event> events,
                        @JsonProperty("event_source_name") final String eventSourceName,
                        @JsonProperty("prioritisation") final String prioritisation) {
        this.name = name;
        this.events = events;
        this.eventSourceName = eventSourceName;
        this.prioritisation = prioritisation;
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public String getPrioritisation() {
        return prioritisation;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Subscription that = (Subscription) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(events, that.events) &&
                Objects.equals(prioritisation, that.prioritisation) &&
                Objects.equals(eventSourceName, that.eventSourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, events,prioritisation, eventSourceName);
    }

    @Override
    public String toString() {
        return "Subscription{" +
               "name='" + name + '\'' +
               ", events=" + events +
               ", eventSourceName='" + eventSourceName + '\'' +
               ", prioritisation='" + prioritisation + '\'' +
               '}';
    }
}
