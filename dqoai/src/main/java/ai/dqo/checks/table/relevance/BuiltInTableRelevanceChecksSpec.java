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
package ai.dqo.checks.table.relevance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ai.dqo.metadata.basespecs.AbstractSpec;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMap;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.metadata.id.HierarchyNodeResultVisitor;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Container of built-in preconfigured relevance checks executed on a table level.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class BuiltInTableRelevanceChecksSpec extends AbstractSpec {
    public static final ChildHierarchyNodeFieldMapImpl<BuiltInTableRelevanceChecksSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractSpec.FIELDS) {
        {
            put("moving_week_average", o -> o.movingWeekAverage);
        }
    };

    @JsonPropertyDescription("Verifies that the average (SELECT AVG(<column>) OVER(ORDER BY <date> DESC ROWS BETWEEN CURRENT ROW AND 6 FOLLOWING ...) meets the required rules, like a minimum row count.")
    private TableRelevanceMovingWeekAverageCheckSpec movingWeekAverage;

    /**
     * Returns a moving week average check.
     * @return Moving week average check.
     */
    public TableRelevanceMovingWeekAverageCheckSpec getMovingWeekAverage() {
        return movingWeekAverage;
    }

    /**
     * Sets a new definition of a moving week average check.
     * @param movingWeekAverage Moving week average check.
     */
    public void setmovingWeekAverage(TableRelevanceMovingWeekAverageCheckSpec movingWeekAverage) {
        this.setDirtyIf(!Objects.equals(this.movingWeekAverage, movingWeekAverage));
        this.movingWeekAverage = movingWeekAverage;
        propagateHierarchyIdToField(movingWeekAverage, "moving_week_average");
    }

    /**
     * Returns the child map on the spec class with all fields.
     *
     * @return Return the field map.
     */
    @Override
    protected ChildHierarchyNodeFieldMap getChildMap() {
        return FIELDS;
    }

    /**
     * Calls a visitor (using a visitor design pattern) that returns a result.
     *
     * @param visitor   Visitor instance.
     * @param parameter Additional parameter that will be passed back to the visitor.
     * @return Result value returned by an "accept" method of the visitor.
     */
    @Override
    public <P, R> R visit(HierarchyNodeResultVisitor<P, R> visitor, P parameter) {
        return visitor.accept(this, parameter);
    }
}
