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
package ai.dqo.utils.tables;

import ai.dqo.BaseTest;
import ai.dqo.data.readings.factory.SensorReadingTableFactoryObjectMother;
import ai.dqo.data.readings.normalization.SensorNormalizedResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

@SpringBootTest
public class TableMergeUtilityTests extends BaseTest {
    private Table currentTable;
    private Table newTable;
    private String[] joinColumnNames;

    /**
     * Called before each test.
     * This method should be overridden in derived super classes (test classes), but remember to add {@link BeforeEach} annotation in a derived test class. JUnit5 demands it.
     *
     * @throws Throwable
     */
    @Override
    @BeforeEach
    protected void setUp() throws Throwable {
        super.setUp();
		this.currentTable = SensorReadingTableFactoryObjectMother.createEmptyNormalizedTable("current");
		this.newTable = SensorReadingTableFactoryObjectMother.createEmptyNormalizedTable("new");
		this.joinColumnNames = new String[] {
                SensorNormalizedResult.CHECK_HASH_COLUMN_NAME,
                SensorNormalizedResult.DIMENSION_ID_COLUMN_NAME
        };
    }

    void addRows(Table targetTable, long checkId, long dimensionId, double actualValue) {
        Row row = targetTable.appendRow();
        row.setDouble(SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME, actualValue);
        row.setLong(SensorNormalizedResult.CHECK_HASH_COLUMN_NAME, checkId);
        row.setLong(SensorNormalizedResult.DIMENSION_ID_COLUMN_NAME, dimensionId);
    }

    @Test
    void mergeNewResults_whenCurrentEmptyAndNewHasRow_thenRowAppended() {
		addRows(newTable, 10L, 20L, 15.0);

        Table merged = TableMergeUtility.mergeNewResults(this.currentTable, this.newTable, this.joinColumnNames);

        Assertions.assertEquals(1, merged.rowCount());
        Assertions.assertEquals("10", merged.getString(0, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("15", merged.getString(0, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));
    }

    @Test
    void mergeNewResults_whenCurrentHasRowAndNewHasNonMatchingRow_thenRowAppendedAndCurrentRetained() {
		addRows(currentTable, 11L, 21L, 16.0);
		addRows(newTable, 10L, 20L, 15.0);

        Table merged = TableMergeUtility.mergeNewResults(this.currentTable, this.newTable, this.joinColumnNames);

        Assertions.assertEquals(2, merged.rowCount());
        Assertions.assertEquals("11", merged.getString(0, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("16", merged.getString(0, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));

        Assertions.assertEquals("10", merged.getString(1, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("15", merged.getString(1, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));
    }

    @Test
    void mergeNewResults_whenCurrentHasRowAndNewHasNonMatchingRowBecauseOneJoinColumnDiffers_thenRowAppendedAndCurrentRetained() {
		addRows(currentTable, 10L, 21L, 16.0);
		addRows(newTable, 10L, 20L, 15.0);

        Table merged = TableMergeUtility.mergeNewResults(this.currentTable, this.newTable, this.joinColumnNames);

        Assertions.assertEquals(2, merged.rowCount());
        Assertions.assertEquals("10", merged.getString(0, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("16", merged.getString(0, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));

        Assertions.assertEquals("10", merged.getString(1, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("15", merged.getString(1, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));
    }

    @Test
    void mergeNewResults_whenCurrentHasRowAndNewHasRowThatOverrides_thenRowReplaced() {
		addRows(currentTable, 10L, 20L, 16.0);
		addRows(newTable, 10L, 20L, 15.0);

        Table merged = TableMergeUtility.mergeNewResults(this.currentTable, this.newTable, this.joinColumnNames);

        Assertions.assertEquals(1, merged.rowCount());
        Assertions.assertEquals("10", merged.getString(0, SensorNormalizedResult.CHECK_HASH_COLUMN_NAME));
        Assertions.assertEquals("15", merged.getString(0, SensorNormalizedResult.ACTUAL_VALUE_COLUMN_NAME));
    }
}
