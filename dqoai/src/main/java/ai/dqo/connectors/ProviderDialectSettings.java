/*
 * Copyright © 2021 DQO.ai (support@dqo.ai)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.dqo.connectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;

/**
 * Information about the dialect of the target database, like the beginning and ending quotes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = false)
public class ProviderDialectSettings {
    private String quoteBegin = "\"";
    private String quoteEnd = "\"";
    private String quoteEscape = "\"\"";
    private boolean tableNameIncludesDatabaseName = false;

    public ProviderDialectSettings() {
    }

    /**
     * Creates a dialect settings.
     * @param quoteBegin Begin quote.
     * @param quoteEnd End quote.
     * @param quoteEscape Quote escape sequence to replace the end quote.
     * @param tableNameIncludesDatabaseName The fully qualified table name should include a database name.
     */
    public ProviderDialectSettings(String quoteBegin, String quoteEnd, String quoteEscape, boolean tableNameIncludesDatabaseName) {
        this.quoteBegin = quoteBegin;
        this.quoteEnd = quoteEnd;
        this.quoteEscape = quoteEscape;
        this.tableNameIncludesDatabaseName = tableNameIncludesDatabaseName;
    }

    /**
     * Quote beginning character.
     * @return Quote beginning character.
     */
    public String getQuoteBegin() {
        return quoteBegin;
    }

    /**
     * Sets the quote beginning character.
     * @param quoteBegin Quote beginning.
     */
    public void setQuoteBegin(String quoteBegin) {
        this.quoteBegin = quoteBegin;
    }

    /**
     * Quote ending character.
     * @return Quote ending.
     */
    public String getQuoteEnd() {
        return quoteEnd;
    }

    /**
     * Sets the quote ending character.
     * @param quoteEnd Quote ending.
     */
    public void setQuoteEnd(String quoteEnd) {
        this.quoteEnd = quoteEnd;
    }

    /**
     * End quote escape sequence that is used when the end quote was found in the identifier and the end quote must be replaced.
     * @return Quote escape sequence.
     */
    public String getQuoteEscape() {
        return quoteEscape;
    }

    /**
     * Sets the quote escape sequence.
     * @param quoteEscape Quote escape sequence.
     */
    public void setQuoteEscape(String quoteEscape) {
        this.quoteEscape = quoteEscape;
    }

    /**
     * Returns if the fully qualified table name should be made of three elements: database.schema.table.
     * The database name is taken from the connection configuration.
     * @return The table must include a database name.
     */
    public boolean isTableNameIncludesDatabaseName() {
        return tableNameIncludesDatabaseName;
    }

    /**
     * Configures if a fully qualified table should be made of three elements: database.schema.table or only schema.table.
     * @param tableNameIncludesDatabaseName True when three elements (also a database) must be rendered, false otherwise.
     */
    public void setTableNameIncludesDatabaseName(boolean tableNameIncludesDatabaseName) {
        this.tableNameIncludesDatabaseName = tableNameIncludesDatabaseName;
    }

    /**
     * Quotes a given identifier.
     * @param identifier Identifier to be quoted.
     * @return Quoted identifier.
     */
    public String quoteIdentifier(String identifier) {
        if (Strings.isNullOrEmpty(identifier)) {
            return identifier;
        }

        return this.quoteBegin + identifier.replace(this.quoteEnd, this.quoteEscape) + this.quoteEnd;
    }
}
