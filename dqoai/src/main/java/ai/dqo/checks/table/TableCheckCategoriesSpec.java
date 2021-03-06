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
package ai.dqo.checks.table;

import ai.dqo.checks.table.consistency.BuiltInTableConsistencyChecksSpec;
import ai.dqo.checks.table.custom.CustomTableCheckSpecMap;
import ai.dqo.checks.table.timeliness.BuiltInTableTimelinessChecksSpec;
import ai.dqo.checks.table.validity.BuiltInTableValidityChecksSpec;
import ai.dqo.metadata.basespecs.AbstractSpec;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMap;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.metadata.id.HierarchyNodeResultVisitor;
import ai.dqo.utils.serialization.IgnoreEmptyYamlSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Container of table level checks that are activated on a table level.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class TableCheckCategoriesSpec extends AbstractSpec {
    public static final ChildHierarchyNodeFieldMapImpl<TableCheckCategoriesSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractSpec.FIELDS) {
        {
            put("validity", o -> o.validity);
            put("consistency", o -> o.consistency);
            put("custom", o -> o.custom);
            put("timeliness", o -> o.timeliness);

        }
    };

    @JsonPropertyDescription("Configuration of validity checks on a table level. Validity checks verify hard rules on the data using static rules, like a minimum row count.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInTableValidityChecksSpec validity = new BuiltInTableValidityChecksSpec();

    @JsonPropertyDescription("Configuration of consistency checks on a table level. Consistency checks detect anomalies like rapid row count changes.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInTableConsistencyChecksSpec consistency = new BuiltInTableConsistencyChecksSpec();

    @JsonPropertyDescription("Configuration of timeliness checks on a table level. Timeliness checks detect anomalies like rapid row count changes.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInTableTimelinessChecksSpec timeliness = new BuiltInTableTimelinessChecksSpec();

    @JsonPropertyDescription("Custom data quality checks configured as a dictionary of sensors. Pick a friendly (business relevant) sensor name as a key and configure the sensor and rules for it.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private CustomTableCheckSpecMap custom;

    /**
     * Returns the validity check configuration on a table level.
     * @return Validity check configuration.
     */
    public BuiltInTableValidityChecksSpec getValidity() {
        return validity;
    }

    /**
     * Sets the validity check configuration on a table level.
     * @param validity New validity checks configuration.
     */
    public void setValidity(BuiltInTableValidityChecksSpec validity) {
		this.setDirtyIf(!Objects.equals(this.validity, validity));
        this.validity = validity;
		this.propagateHierarchyIdToField(validity, "validity");
    }

    /**
     * Built-in consistency checks that verify if the table data changes in a steady way.
     * @return Consistency data quality checks on a table level.
     */
    public BuiltInTableConsistencyChecksSpec getConsistency() {
        return consistency;
    }

    /**
     * Sets a new object with consistency table level checks.
     * @param consistency New consistency checks.
     */
    public void setConsistency(BuiltInTableConsistencyChecksSpec consistency) {
		this.setDirtyIf(!Objects.equals(this.consistency, consistency));
        this.consistency = consistency;
		this.propagateHierarchyIdToField(consistency, "consistency");
    }

    /**
     * Built-in timeliness checks that verify if the table data changes in a steady way.
     * @return Consistency data quality checks on a table level.
     */
    public BuiltInTableTimelinessChecksSpec getTimeliness() {
        return timeliness;
    }

    /**
     * Sets a new object with timeliness table level checks.
     * @param timeliness New consistency checks.
     */
    public void setTimeliness(BuiltInTableTimelinessChecksSpec timeliness) {
        this.setDirtyIf(!Objects.equals(this.timeliness, timeliness));
        this.timeliness = timeliness;
        this.propagateHierarchyIdToField(timeliness, "timeliness");
    }

    /**
     * Returns a dictionary of custom sensors.
     * @return Custom sensors map.
     */
    public CustomTableCheckSpecMap getCustom() {
        return custom;
    }

    /**
     * Sets a dictionary of custom sensors.
     * @param custom Custom sensors map.
     */
    public void setCustom(CustomTableCheckSpecMap custom) {
		this.setDirtyIf(!Objects.equals(this.custom, custom));
        this.custom = custom;
		this.propagateHierarchyIdToField(custom, "custom");
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
