package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.EmptySchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmptyAcceptorTest {

    @InjectMocks
    private EmptyAcceptor emptyAcceptor;

    @Test
    public void shouldVisitEmptySchema() throws Exception {
        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final EmptySchema schema = mock(EmptySchema.class);

        emptyAcceptor.accept(fieldName, schema, visitor);

        verify(visitor).visit(fieldName, schema);
    }
}
