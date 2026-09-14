package uk.gov.justice.services.liquibase.postgres;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.core.AddColumnChange;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.AddColumnStatement;

/**
 * Discards the <code>afterColumn</code> hint on databases that have no column ordering.
 *
 * <p>{@code afterColumn} means "add this column immediately after that one". Only MySQL can honour
 * it; PostgreSQL has no column ordering, so on our databases it has never done anything.
 *
 * <p>Liquibase 4 discarded it. {@code AddColumnChange.generateStatements} read:
 *
 * <pre>
 *     if ((database instanceof MySQLDatabase) &amp;&amp; (column.getAfterColumn() != null)) {
 *         addColumnStatement.setAddAfterColumn(column.getAfterColumn());
 *     }
 * </pre>
 *
 * <p>Liquibase 5.0.3 dropped that guard and sets the field unconditionally. Two things then break on
 * PostgreSQL:
 *
 * <ol>
 *   <li>{@code AddColumnGenerator.validate} calls {@code checkDisallowedField("addAfterColumn", ...)}
 *       and the migration aborts with
 *       <em>"addAfterColumn is not allowed on postgresql"</em>, so nothing is applied at all;</li>
 *   <li>{@code AddColumnGenerator.generateSql} appends {@code " AFTER <column>"} with no check on the
 *       database, which would be invalid PostgreSQL if the validation were simply suppressed.</li>
 * </ol>
 *
 * <p>Clearing the field on the generated statement fixes both at source: there is nothing left to
 * reject and nothing left to render. Suppressing the validation error alone would be actively worse
 * than the current failure, turning a clean refusal into a broken {@code ALTER TABLE}.
 *
 * <p>This exists because existing changesets cannot be edited. They are recorded in
 * DATABASECHANGELOG with a checksum, and altering one invalidates every environment that has already
 * run it.
 *
 * <p>Registered through {@code META-INF/services/liquibase.change.Change}. {@link
 * liquibase.change.ChangeFactory} picks the highest-priority implementation for a given change name,
 * so this supersedes the built-in {@code addColumn} while inheriting all of its behaviour.
 */
@DatabaseChange(
        name = "addColumn",
        priority = ChangeMetaData.PRIORITY_DEFAULT + 1,
        appliesTo = "table",
        description = "Adds a new column to an existing table, ignoring afterColumn where the database has no column ordering")
public class ColumnOrderingAgnosticAddColumnChange extends AddColumnChange {

    @Override
    public SqlStatement[] generateStatements(final Database database) {

        final SqlStatement[] statements = super.generateStatements(database);

        if (!supportsColumnOrdering(database)) {
            for (final SqlStatement statement : statements) {
                clearAfterColumn(statement);
            }
        }

        return statements;
    }

    private boolean supportsColumnOrdering(final Database database) {
        return database instanceof MySQLDatabase;
    }

    private void clearAfterColumn(final SqlStatement statement) {

        if (statement instanceof final AddColumnStatement addColumnStatement) {

            // Liquibase uses null to mean "no afterColumn was given": checkDisallowedField only
            // objects when the value is non-null, and generateSql only appends AFTER when it is
            // non-null and non-empty. An empty string would silence the SQL but not the validation.
            addColumnStatement.setAddAfterColumn(null);

            // A multi-column addColumn carries one nested statement per column, and it is those the
            // generator validates and renders, so they need clearing too.
            addColumnStatement.getColumns().forEach(this::clearAfterColumn);
        }
    }
}
