package uk.gov.justice.generation.pojo.visitor;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ENUM;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDefinitionFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldConstructRootClassDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final Definition definition = new DefaultDefinitionFactory().constructRootClassDefinition(fieldName);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(ROOT));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructClassDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, objectSchema);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(CLASS));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructCombinedDefinition() throws Exception {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, combinedSchema);

        assertThat(definition, is(instanceOf(CombinedDefinition.class)));
        assertThat(definition.type(), is(COMBINED));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForArraySchema() throws Exception {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, arraySchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(ARRAY));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructEnumDefinition() throws Exception {
        final String fieldName = "fieldName";
        final String enumValue = "test value";
        final EnumSchema enumSchema = EnumSchema.builder().possibleValue(enumValue).build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, enumSchema);

        assertThat(definition, is(instanceOf(EnumDefinition.class)));
        assertThat(definition.type(), is(ENUM));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((EnumDefinition) definition).getEnumValues(), hasItem(enumValue));
    }

    @Test
    public void shouldConstructFieldDefinitionForStringSchemaWithDescriptionOfTheClassName() throws Exception {
        final String fieldName = "fieldName";
        final StringSchema stringSchema = StringSchema.builder().description("UUID").build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, stringSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(STRING));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForBooleanSchema() throws Exception {
        final String fieldName = "fieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, booleanSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(BOOLEAN));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForNumberSchema() throws Exception {
        final String fieldName = "fieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, numberSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(NUMBER));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForIntegerNumberSchema() throws Exception {
        final String fieldName = "fieldName";
        final NumberSchema numberSchema = NumberSchema.builder().requiresInteger(true).build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, numberSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(INTEGER));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructReferenceDefinition() throws Exception {
        final String fieldName = "fieldName";
        final String referenceValue = "reference value";
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().refValue(referenceValue).build();

        final Definition definition = new DefaultDefinitionFactory().constructDefinitionFor(fieldName, referenceSchema);

        assertThat(definition, is(instanceOf(ReferenceDefinition.class)));
        assertThat(definition.type(), is(REFERENCE));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((ReferenceDefinition) definition).getReferenceValue(), is(referenceValue));
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessedForConstructDefintion() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final String packageName = "package.name";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);
        new DefaultDefinitionFactory().constructDefinitionFor(fieldName, dummySchema);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessedForConstructFieldDefintion() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);
        new DefaultDefinitionFactory().constructDefinitionFor("fieldName", dummySchema);
    }

    private class DummySchema extends Schema {

        private final String id;

        DummySchema(final Builder<?> builder, final String id) {
            super(builder);
            this.id = id;
        }

        @Override
        public void validate(final Object o) {
            //do nothing
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
