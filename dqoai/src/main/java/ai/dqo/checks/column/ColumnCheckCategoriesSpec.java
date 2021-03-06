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
package ai.dqo.checks.column;

import ai.dqo.checks.column.completeness.BuiltInColumnCompletenessChecksSpec;
import ai.dqo.checks.column.custom.CustomColumnCheckSpecMap;
import ai.dqo.checks.column.uniqueness.BuiltInColumnUniquenessChecksSpec;
import ai.dqo.checks.column.validity.BuiltInColumnValidityChecksSpec;
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
 * Container of column level, preconfigured checks.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class ColumnCheckCategoriesSpec extends AbstractSpec {
    public static final ChildHierarchyNodeFieldMapImpl<ColumnCheckCategoriesSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractSpec.FIELDS) {
        {
			put("validity", o -> o.validity);
			put("uniqueness", o -> o.uniqueness);
            put("completeness", o -> o.completeness);
			put("custom", o -> o.custom);
        }
    };

    @JsonPropertyDescription("Configuration of validity checks on a column level. Validity checks verify hard rules on the data using static rules like valid column value ranges.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInColumnValidityChecksSpec validity;

    @JsonPropertyDescription("Configuration of uniqueness checks on a table level. Uniqueness checks verify that the column values are unique or the percentage of duplicates is acceptable.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInColumnUniquenessChecksSpec uniqueness = new BuiltInColumnUniquenessChecksSpec();

    //TODO add description
    @JsonPropertyDescription("Configuration of completeness checks on a column level. Completeness checks verify...")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private BuiltInColumnCompletenessChecksSpec completeness;

    @JsonPropertyDescription("Custom data quality checks configured as a dictionary of sensors. Pick a friendly (business relevant) sensor name as a key and configure the sensor and rules for it.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = IgnoreEmptyYamlSerializer.class)
    private CustomColumnCheckSpecMap custom;


    /**
     * Returns the validity check configuration on a column level.
     * @return Validity check configuration.
     */
    public BuiltInColumnValidityChecksSpec getValidity() {
        return validity;
    }

    /**
     * Sets the validity check configuration on a column level.
     * @param validity New validity checks configuration.
     */
    public void setValidity(BuiltInColumnValidityChecksSpec validity) {
		this.setDirtyIf(!Objects.equals(this.validity, validity));
        this.validity = validity;
		this.propagateHierarchyIdToField(validity, "validity");
    }

    /**
     * Column uniqueness checks.
     * @return Column uniqueness checks.
     */
    public BuiltInColumnUniquenessChecksSpec getUniqueness() {
        return uniqueness;
    }

    /**
     * Sets the set of column uniqueness checks.
     * @param uniqueness Column uniqueness checks.
     */
    public void setUniqueness(BuiltInColumnUniquenessChecksSpec uniqueness) {
		this.setDirtyIf(!Objects.equals(this.uniqueness, uniqueness));
        this.uniqueness = uniqueness;
		this.propagateHierarchyIdToField(uniqueness, "uniqueness");
    }

    /**
     * Returns a dictionary of custom sensors.
     * @return Custom sensors map.
     */
    public CustomColumnCheckSpecMap getCustom() {
        return custom;
    }

    /**
     * Sets a dictionary of custom sensors.
     * @param custom Custom sensors map.
     */
    public void setCustom(CustomColumnCheckSpecMap custom) {
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
