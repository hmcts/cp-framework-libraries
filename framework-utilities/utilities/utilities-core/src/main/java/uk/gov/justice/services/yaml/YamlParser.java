package uk.gov.justice.services.yaml;

import static java.lang.String.format;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.type.TypeReference;

public class YamlParser {

    public <T> T parseYamlFrom(final URL yamlUrl, final TypeReference typeReference) {
        try {
            return new ObjectMapperProducer().yamlObjectMapper().readValue(yamlUrl, typeReference);
        } catch (final IOException e) {
            throw new YamlParserException(format("Failed to read YAML file %s ", yamlUrl), e);
        }
    }

    public <T> T parseYamlFrom(final URL yamlUrl, final Class<T> classType) {
        try {
            return new ObjectMapperProducer().yamlObjectMapper().readValue(yamlUrl, classType);

        } catch (final IOException e) {
            throw new YamlParserException(format("Failed to read YAML file %s ", yamlUrl), e);
        }
    }
}
