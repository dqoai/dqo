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
package ai.dqo.checks.column.completeness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ai.dqo.checks.AbstractCheckSpec;
import ai.dqo.checks.AbstractRuleSetSpec;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMap;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.sensors.AbstractSensorParametersSpec;
import ai.dqo.sensors.column.completeness.ColumnCompletenessMissingDatesCountSensorParametersSpec;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Column missing dates count check that counts the number of missing dates in a column.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class ColumnCompletenessMissingDatesCountCheckSpec extends AbstractCheckSpec {
    public static final ChildHierarchyNodeFieldMapImpl<ColumnCompletenessMissingDatesCountCheckSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractCheckSpec.FIELDS) {
        {
            put("parameters", o -> o.parameters);
            put("rules", o -> o.rules);
        }
    };

    @JsonPropertyDescription("Missing dates count sensor parameters")
    private ColumnCompletenessMissingDatesCountSensorParametersSpec parameters = new ColumnCompletenessMissingDatesCountSensorParametersSpec();

    @JsonPropertyDescription("Missing dates count validation rules at various alert severity levels (thresholds)")
    private ColumnCompletenessMissingDatesCountRulesSpec rules = new ColumnCompletenessMissingDatesCountRulesSpec();

    /**
     * Returns the parameters of the sensor.
     * @return Sensor parameters.
     */
    public ColumnCompletenessMissingDatesCountSensorParametersSpec getParameters() {
        return parameters;
    }

    /**
     * Sets a new row count sensor parameter object.
     * @param parameters Row count parameters.
     */
    public void setParameters(ColumnCompletenessMissingDatesCountSensorParametersSpec parameters) {
        this.setDirtyIf(!Objects.equals(this.parameters, parameters));
        this.parameters = parameters;
        this.propagateHierarchyIdToField(parameters, "parameters");
    }

    /**
     * Returns rules for the check.
     * @return Rules set for the check.
     */
    public ColumnCompletenessMissingDatesCountRulesSpec getRules() {
        return rules;
    }

    /**
     * Sets a rules set for the check.
     * @param rules Rules set.
     */
    public void setRules(ColumnCompletenessMissingDatesCountRulesSpec rules) {
        this.setDirtyIf(!Objects.equals(this.rules, rules));
        this.rules = rules;
        this.propagateHierarchyIdToField(rules, "rules");
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
     * Returns the sensor parameters spec object that identifies the sensor definition to use and contains parameters.
     *
     * @return Sensor parameters.
     */
    @Override
    @JsonIgnore
    public AbstractSensorParametersSpec getSensorParameters() {
        return this.parameters;
    }

    /**
     * Returns a rule set for this check.
     *
     * @return Rule set.
     */
    @JsonIgnore
    @Override
    public AbstractRuleSetSpec getRuleSet() {
        return this.rules;
    }
}
