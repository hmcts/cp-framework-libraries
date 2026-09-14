package uk.gov.justice.services.liquibase.postgres;

import static java.util.Arrays.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import liquibase.change.AddColumnConfig;
import liquibase.change.ChangeFactory;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.AddColumnStatement;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class ColumnOrderingAgnosticAddColumnChangeTest {

    private static final String TABLE = "cpp_user";
    private static final String NEW_COLUMN = "organisation_id";
    private static final String EXISTING_COLUMN = "user_id";
    private static final String SECOND_NEW_COLUMN = "account_status";

    @Test
    void shouldDiscardAfterColumnOnPostgres() {

        final SqlStatement[] statements = addColumnChange(NEW_COLUMN).generateStatements(new PostgresDatabase());

        addColumnStatements(statements)
                .forEach(statement -> assertThat(statement.getAddAfterColumn(), is(nullValue())));
    }

    @Test
    void shouldKeepAfterColumnOnMysqlWhereItIsMeaningful() {

        final SqlStatement[] statements = addColumnChange(NEW_COLUMN).generateStatements(new MySQLDatabase());

        addColumnStatements(statements)
                .forEach(statement -> assertThat(statement.getAddAfterColumn(), is(EXISTING_COLUMN)));
    }

    @Test
    void shouldDiscardAfterColumnOnEveryColumnOfAMultiColumnChange() {

        final ColumnOrderingAgnosticAddColumnChange change = addColumnChange(NEW_COLUMN);
        change.addColumn(column(SECOND_NEW_COLUMN));

        final SqlStatement[] statements = change.generateStatements(new PostgresDatabase());

        addColumnStatements(statements).forEach(statement -> {
            assertThat(statement.getAddAfterColumn(), is(nullValue()));
            statement.getColumns()
                    .forEach(nested -> assertThat(nested.getAddAfterColumn(), is(nullValue())));
        });
    }

    @Test
    void shouldProduceNoValidationErrorsOnPostgres() {

        final Database database = new PostgresDatabase();
        final SqlStatement[] statements = addColumnChange(NEW_COLUMN).generateStatements(database);

        for (final SqlStatement statement : statements) {
            final ValidationErrors errors = SqlGeneratorFactory.getInstance().validate(statement, database);
            assertThat(errors.getErrorMessages(), is(emptyIterable()));
        }
    }

    @Test
    void shouldNotRenderAnAfterClauseInTheGeneratedPostgresSql() {

        final Database database = new PostgresDatabase();
        final SqlStatement[] statements = addColumnChange(NEW_COLUMN).generateStatements(database);

        for (final SqlStatement statement : statements) {
            for (final Sql sql : SqlGeneratorFactory.getInstance().generateSql(statement, database)) {
                assertThat(sql.toSql().toUpperCase(), not(containsAfterClause()));
            }
        }
    }

    @Test
    void shouldSupersedeTheBuiltInAddColumnChange() {

        final liquibase.change.Change change = ChangeFactory.getInstance().create("addColumn");

        assertThat(change, is(not(nullValue())));
        assertThat(change.getClass().getName(),
                is(ColumnOrderingAgnosticAddColumnChange.class.getName()));
    }

    private org.hamcrest.Matcher<String> containsAfterClause() {
        return org.hamcrest.Matchers.containsString(" AFTER ");
    }

    private ColumnOrderingAgnosticAddColumnChange addColumnChange(final String columnName) {
        final ColumnOrderingAgnosticAddColumnChange change = new ColumnOrderingAgnosticAddColumnChange();
        change.setTableName(TABLE);
        change.addColumn(column(columnName));
        return change;
    }

    private AddColumnConfig column(final String columnName) {
        final AddColumnConfig column = new AddColumnConfig();
        column.setName(columnName);
        column.setType("UUID");
        column.setAfterColumn(EXISTING_COLUMN);
        return column;
    }

    private Stream<AddColumnStatement> addColumnStatements(final SqlStatement[] statements) {
        return stream(statements)
                .filter(AddColumnStatement.class::isInstance)
                .map(AddColumnStatement.class::cast);
    }
}
