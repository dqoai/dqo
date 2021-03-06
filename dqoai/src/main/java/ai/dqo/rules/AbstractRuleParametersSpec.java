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
package ai.dqo.rules;

import ai.dqo.metadata.basespecs.AbstractSpec;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.metadata.id.HierarchyNodeResultVisitor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;

/**
 * Base class for a quality rule.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRuleParametersSpec extends AbstractSpec {
    public static final ChildHierarchyNodeFieldMapImpl<AbstractRuleParametersSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractSpec.FIELDS) {
        {
        }
    };

    @JsonPropertyDescription("Disable the rule. The rule will not be evaluated. The sensor will also not be executed if it has no enabled rules.")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean disable;

    /**
     * Disable the quality check and prevent it from executing.
     * @return Quality check is disabled.
     */
    public boolean isDisable() {
        return disable;
    }

    /**
     * Changes the disabled flag of a quality test.
     * @param disable When true, the test will be disabled and will not be executed.
     */
    public void setDisable(boolean disable) {
		this.setDirtyIf(this.disable != disable);
        this.disable = disable;
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

    /**
     * Retrieves the severity level for this class. The rule parameters should be always referenced from (parent) a {@link AbstractRuleThresholdsSpec}
     * in fields named: low, medium, high  - and those are the severity levels returned.
     * @return Severity level.
     */
    @JsonIgnore
    public RuleSeverityLevel getSeverityLevel() {
        String severityNameLowerCase = this.getHierarchyId().getLast().toString();
        RuleSeverityLevel ruleSeverityLevel = Enum.valueOf(RuleSeverityLevel.class, severityNameLowerCase);
        return ruleSeverityLevel;
    }

    /**
     * Returns a rule definition name. It is a name of a python module (file) without the ".py" extension. Rule names are related to the "rules" folder in DQO_HOME.
     * @return Rule definition name (python module name without .py extension).
     */
    @JsonIgnore
    public abstract String getRuleDefinitionName();

    /**
     * Checks if the object is a default value, so it would be rendered as an empty node. We want to skip it and not render it to YAML.
     * The implementation of this interface method should check all object's fields to find if at least one of them has a non-default value or is not null, so it should be rendered.
     *
     * @return true when the object has the default values only and should not be rendered to YAML, false when it should be rendered.
     */
    @Override
    public boolean isDefault() {
        return false; // always render
    }
}
