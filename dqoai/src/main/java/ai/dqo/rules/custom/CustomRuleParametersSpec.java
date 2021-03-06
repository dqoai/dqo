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
package ai.dqo.rules.custom;

import ai.dqo.metadata.id.ChildHierarchyNodeFieldMap;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.rules.AbstractRuleParametersSpec;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Custom data quality rule.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class CustomRuleParametersSpec extends AbstractRuleParametersSpec {
    private static final ChildHierarchyNodeFieldMapImpl<CustomRuleParametersSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractRuleParametersSpec.FIELDS) {
        {
        }
    };

    /**
     * Default constructor, the minimum accepted value is 0.
     */
    public CustomRuleParametersSpec() {
    }

    /**
     * Creates a custom rule given the rule name.
     * @param ruleName Rule name.
     */
    public CustomRuleParametersSpec(String ruleName) {
        this.ruleName = ruleName;
    }

    @JsonPropertyDescription("Custom rule name. It is a path to a custom rule python module that starts at the user home rules folder. The path should not end with the .py file extension. Sample rule: myrules/my_custom_rule")
    private String ruleName;


    @JsonPropertyDescription("Dictionary of additional parameters (key / value pairs) that are passed to the rule.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private LinkedHashMap<String, Object> originalParams = new LinkedHashMap<>(); // used to perform comparison in the isDirty check

    /**
     * Returns the rule definition name.
     * @return Rule name.
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets the rule name.
     * @param ruleName Rule name.
     */
    public void setRuleName(String ruleName) {
		this.setDirtyIf(!Objects.equals(this.ruleName, ruleName));
        this.ruleName = ruleName;
    }


    /**
     * Returns a key/value map of additional rule parameters.
     * @return Key/value dictionary of additional parameters.
     */
    public HashMap<String, Object> getParams() {
        return params;
    }

    /**
     * Sets a dictionary of additional rule parameters.
     * @param params Key/value dictionary with extra parameters.
     */
    public void setParams(LinkedHashMap<String, Object> params) {
		setDirtyIf(!Objects.equals(this.params, params));
        this.params = params;
		this.originalParams = (LinkedHashMap<String, Object>) params.clone();
    }

    /**
     * Check if the object is dirty (has changes).
     *
     * @return True when the object is dirty and has modifications.
     */
    @Override
    public boolean isDirty() {
        return super.isDirty() || !Objects.equals(this.params, this.originalParams);
    }

    /**
     * Clears the dirty flag (sets the dirty to false). Called after flushing or when changes should be considered as unimportant.
     * @param propagateToChildren When true, clears also the dirty status of child objects.
     */
    @Override
    public void clearDirty(boolean propagateToChildren) {
        super.clearDirty(propagateToChildren);
		this.originalParams = (LinkedHashMap<String, Object>) this.params.clone();
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
     * Returns a rule definition name. It is a name of a python module (file) without the ".py" extension. Rule names are related to the "rules" folder in DQO_HOME.
     *
     * @return Rule definition name (python module name without .py extension).
     */
    @Override
    public String getRuleDefinitionName() {
        return this.ruleName;
    }
}
