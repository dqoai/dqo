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
package ai.dqo.execution.rules.runners.python;

import ai.dqo.execution.rules.RuleExecutionRunParameters;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;

/**
 * Object passed to the python rule evaluation module. Specifies the path to the python file with the rule implementation and the parameters.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = false)
public class PythonRuleCallInput {
    private String ruleModulePath;
    private RuleExecutionRunParameters ruleParameters;

    /**
     * Returns the path to a rule module (python file).
     * @return Path to the file with the rule implementation.
     */
    public String getRuleModulePath() {
        return ruleModulePath;
    }

    /**
     * Sets a path to the rule module.
     * @param ruleModulePath Rule module path.
     */
    public void setRuleModulePath(String ruleModulePath) {
        this.ruleModulePath = ruleModulePath;
    }

    /**
     * Returns the rule parameters that will be evaluated.
     * @return Rule parameters.
     */
    public RuleExecutionRunParameters getRuleParameters() {
        return ruleParameters;
    }

    /**
     * Sets the rule parameter object.
     * @param ruleParameters Rule evaluation parameter object.
     */
    public void setRuleParameters(RuleExecutionRunParameters ruleParameters) {
        this.ruleParameters = ruleParameters;
    }
}
