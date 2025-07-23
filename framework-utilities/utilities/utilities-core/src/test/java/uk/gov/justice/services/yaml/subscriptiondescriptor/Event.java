package uk.gov.justice.services.yaml.subscriptiondescriptor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    private final String name;
    private final String schemaUri;

    @JsonCreator
    public Event(@JsonProperty("name") final String name, @JsonProperty("schema_uri") final String schemaUri) {
        this.name = name;
        this.schemaUri = schemaUri;
    }

    public String getName() {
        return name;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    @Override
    public String toString() {
        return "Event{" +
               "name='" + name + '\'' +
               ", schemaUri='" + schemaUri + '\'' +
               '}';
    }
}
