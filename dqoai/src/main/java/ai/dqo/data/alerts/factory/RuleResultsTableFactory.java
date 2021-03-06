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
package ai.dqo.data.alerts.factory;

import tech.tablesaw.api.Table;

/**
 * Factory that creates an empty tablesaw table for storing the rule evaluation results. The table schema is configured.
 */
public interface RuleResultsTableFactory {
    /**
     * Creates an empty normalized rule result (alerts) table that has the right schema.
     *
     * @param tableName Table name.
     * @return Empty rule evaluation results (alerts) table.
     */
    Table createEmptyRuleResultsTable(String tableName);
}
