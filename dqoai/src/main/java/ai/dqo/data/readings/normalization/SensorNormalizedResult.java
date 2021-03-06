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
package ai.dqo.data.readings.normalization;

import tech.tablesaw.api.*;

/**
 * Describes the dataset (dataframe) returned from the sensor. Identifies the time series column, dimension columns, etc.
 * The columns are normalized.
 */
public class SensorNormalizedResult {
    /**
     * Column name that stores the actual value: actual_value.
     */
    public static final String ACTUAL_VALUE_COLUMN_NAME = "actual_value";

    /**
     * Column name that stores the expected value (expected_value). It is an optional column used when the sensor will also retrieve a comparison value (for accuracy checks).
     */
    public static final String EXPECTED_VALUE_COLUMN_NAME = "expected_value";

    /**
     * Column name that stores the time period of the reading (timestamp): timestamp_period.
     */
    public static final String TIME_PERIOD_COLUMN_NAME = "time_period";

    /**
     * Column name for a time gradient.
     */
    public static final String TIME_GRADIENT_COLUMN_NAME = "time_gradient";

    /**
     * Column name prefix for dimension columns: dimension_.
     */
    public static final String DIMENSION_COLUMN_NAME_PREFIX = "dimension_";

    /**
     * Column name for a dimension id, it is a hash of the dimension names.
     */
    public static final String DIMENSION_ID_COLUMN_NAME = "dimension_id";

    /**
     * Column name for a connection hash.
     */
    public static final String CONNECTION_HASH_COLUMN_NAME = "connection_hash";

    /**
     * Column name for a connection name.
     */
    public static final String CONNECTION_NAME_COLUMN_NAME = "connection_name";

    /**
     * Column name for a provider name.
     */
    public static final String PROVIDER_COLUMN_NAME = "provider";

    /**
     * Column name for a table hash.
     */
    public static final String TABLE_HASH_COLUMN_NAME = "table_hash";

    /**
     * Column name for a table schema.
     */
    public static final String SCHEMA_NAME_COLUMN_NAME = "schema_name";

    /**
     * Column name for a table name.
     */
    public static final String TABLE_NAME_COLUMN_NAME = "table_name";

    /**
     * Column name for a table stage.
     */
    public static final String TABLE_STAGE_COLUMN_NAME = "table_stage";

    /**
     * Column name for a column hash.
     */
    public static final String COLUMN_HASH_COLUMN_NAME = "column_hash";

    /**
     * Column name for a column name.
     */
    public static final String COLUMN_NAME_COLUMN_NAME = "column_name";

    /**
     * Column name for a check hash.
     */
    public static final String CHECK_HASH_COLUMN_NAME = "check_hash";

    /**
     * Column name for a check name.
     */
    public static final String CHECK_NAME_COLUMN_NAME = "check_name";

    /**
     * Column name for a quality dimension.
     */
    public static final String QUALITY_DIMENSION_COLUMN_NAME = "quality_dimension";

    /**
     * Column name for a sensor name.
     */
    public static final String SENSOR_NAME_COLUMN_NAME = "sensor_name";

    /**
     * Column name for a sensor executed at timestamp.
     */
    public static final String EXECUTED_AT_COLUMN_NAME = "executed_at";

    /**
     * Column name for a sensor duration in milliseconds.
     */
    public static final String DURATION_MS_COLUMN_NAME = "duration_ms";


    private final Table table;
    private final DoubleColumn actualValueColumn;
    private final DoubleColumn expectedValueColumn;
    private final DateTimeColumn timePeriodColumn;
    private final StringColumn timeGradientColumn;
    private final LongColumn dimensionIdColumn;
    private final LongColumn connectionHashColumn;
    private final StringColumn connectionNameColumn;
    private final StringColumn providerColumn;
    private final LongColumn tableHashColumn;
    private final StringColumn schemaNameColumn;
    private final StringColumn tableNameColumn;
    private final StringColumn tableStageColumn;
    private final LongColumn columnHashColumn;
    private final StringColumn columnNameColumn;
    private final LongColumn checkHashColumn;
    private final StringColumn checkNameColumn;
    private final StringColumn qualityDimensionColumn;
    private final StringColumn sensorNameColumn;
    private final InstantColumn executedAtColumn;
    private final IntColumn durationMsColumn;

    /**
     * Creates a sensor result dataset, extracting key columns.
     * @param table Sorted table with sensor results.
     */
    public SensorNormalizedResult(Table table) {
        this.table = table;
		this.actualValueColumn = (DoubleColumn) table.column(ACTUAL_VALUE_COLUMN_NAME);
		this.expectedValueColumn = (DoubleColumn) table.column(EXPECTED_VALUE_COLUMN_NAME);
		this.timePeriodColumn = (DateTimeColumn) table.column(TIME_PERIOD_COLUMN_NAME);
		this.timeGradientColumn = (StringColumn) table.column(TIME_GRADIENT_COLUMN_NAME);
		this.dimensionIdColumn = (LongColumn) table.column(DIMENSION_ID_COLUMN_NAME);
		this.connectionHashColumn = (LongColumn) table.column(CONNECTION_HASH_COLUMN_NAME);
        this.connectionNameColumn = (StringColumn) table.column(CONNECTION_NAME_COLUMN_NAME);
        this.providerColumn = (StringColumn) table.column(PROVIDER_COLUMN_NAME);
		this.tableHashColumn = (LongColumn) table.column(TABLE_HASH_COLUMN_NAME);
        this.schemaNameColumn = (StringColumn) table.column(SCHEMA_NAME_COLUMN_NAME);
        this.tableNameColumn = (StringColumn) table.column(TABLE_NAME_COLUMN_NAME);
        this.tableStageColumn = (StringColumn) table.column(TABLE_STAGE_COLUMN_NAME);
		this.columnHashColumn = (LongColumn) table.column(COLUMN_HASH_COLUMN_NAME);
        this.columnNameColumn = (StringColumn) table.column(COLUMN_NAME_COLUMN_NAME);
		this.checkHashColumn = (LongColumn) table.column(CHECK_HASH_COLUMN_NAME);
        this.checkNameColumn = (StringColumn) table.column(CHECK_NAME_COLUMN_NAME);
        this.qualityDimensionColumn = (StringColumn) table.column(QUALITY_DIMENSION_COLUMN_NAME);
        this.sensorNameColumn = (StringColumn) table.column(SENSOR_NAME_COLUMN_NAME);
		this.executedAtColumn = (InstantColumn) table.column(EXECUTED_AT_COLUMN_NAME);
		this.durationMsColumn = (IntColumn) table.column(DURATION_MS_COLUMN_NAME);
    }

    /**
     * Dataset table that was returned from the sensor query.
     * @return Sensor results as a table with a time dimension column and grouping dimension columns.
     */
    public Table getTable() {
        return table;
    }

    /**
     * Actual value column.
     * @return Actual value column.
     */
    public DoubleColumn getActualValueColumn() {
        return actualValueColumn;
    }

    /**
     * Expected value column.
     * @return Optional expected value column. The column may contain nulls.
     */
    public DoubleColumn getExpectedValueColumn() {
        return expectedValueColumn;
    }

    /**
     * Time period column.
     * @return Time period column.
     */
    public DateTimeColumn getTimePeriodColumn() {
        return timePeriodColumn;
    }

    /**
     * Time gradient column that returns the time gradient that was used by the sensor. It is a lower case name of
     * a time gradient from {@link ai.dqo.metadata.groupings.TimeSeriesGradient} enumeration.
     * @return Time gradient name, e.q. "day", "week", "month".
     */
    public StringColumn getTimeGradientColumn() {
        return timeGradientColumn;
    }

    /**
     * Dimension id column.
     * @return Dimension id column.
     */
    public LongColumn getDimensionIdColumn() {
        return dimensionIdColumn;
    }

    /**
     * Connection hash column. The column contains a 64-bit hash of the connection's hierarchy id.
     * @return Connection hash column.
     */
    public LongColumn getConnectionHashColumn() {
        return connectionHashColumn;
    }

    /**
     * Returns a connection name column. The column contains the source connection name.
     * @return Connection name column.
     */
    public StringColumn getConnectionNameColumn() {
        return connectionNameColumn;
    }

    /**
     * Returns a provider name column. The column contains the provider name.
     * @return Provider name column.
     */
    public StringColumn getProviderColumn() {
        return providerColumn;
    }

    /**
     * Table hash column. The column contains a 64-bit hash of the table's hierarchy id.
     * @return Table hash column.
     */
    public LongColumn getTableHashColumn() {
        return tableHashColumn;
    }

    /**
     * Returns a tablesaw column with the schema name of a table.
     * @return Schema name column.
     */
    public StringColumn getSchemaNameColumn() {
        return schemaNameColumn;
    }

    /**
     * Returns a tablesaw column with the table name.
     * @return Column with the table name.
     */
    public StringColumn getTableNameColumn() {
        return tableNameColumn;
    }

    /**
     * Returns a tablesaw column with the table's stage.
     * @return Table's stage column.
     */
    public StringColumn getTableStageColumn() {
        return tableStageColumn;
    }

    /**
     * Column hash column. The column contains a 64-bit hash of the column's hierarchy id.
     * @return Column hash column.
     */
    public LongColumn getColumnHashColumn() {
        return columnHashColumn;
    }

    /**
     * Returns a tablesaw column that stores the column name. The column with "column names" may contain nulls when checks are defined on a whole table level.
     * @return Column name tablesaw column.
     */
    public StringColumn getColumnNameColumn() {
        return columnNameColumn;
    }

    /**
     * Check hash column. The column contains a 64-bit hash of the check's hierarchy id.
     * @return Check hash column.
     */
    public LongColumn getCheckHashColumn() {
        return checkHashColumn;
    }

    /**
     * Returns a tablesaw column with the check name.
     * @return Check name column.
     */
    public StringColumn getCheckNameColumn() {
        return checkNameColumn;
    }

    /**
     * Returns a tablesaw column with the parent quality dimension of a check.
     * @return Quality dimension of a check.
     */
    public StringColumn getQualityDimensionColumn() {
        return qualityDimensionColumn;
    }

    /**
     * Returns a column with the sensor name.
     * @return Sensor name column.
     */
    public StringColumn getSensorNameColumn() {
        return sensorNameColumn;
    }

    /**
     * Absolute timestamp when the sensor execution started.
     * @return Sensor started timestamp.
     */
    public InstantColumn getExecutedAtColumn() {
        return executedAtColumn;
    }

    /**
     * Sensor execution duration in milliseconds.
     * @return Sensor duration in millis.
     */
    public IntColumn getDurationMsColumn() {
        return durationMsColumn;
    }
}
