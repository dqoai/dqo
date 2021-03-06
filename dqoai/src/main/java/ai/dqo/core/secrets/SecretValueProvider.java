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
package ai.dqo.core.secrets;

import java.util.LinkedHashMap;

/**
 * Provider that returns secret values. It can retrieve values from environment variables or in the future, from Secret Managers, Vaults, etc.
 */
public interface SecretValueProvider {
    /**
     * Expands a value that references possible secret values. For example ${ENVIRONMENT_VARIABLE_NAME}
     * @param value Value to expand.
     * @return Value (when no expansions possible) or an expanded value.
     */
    String expandValue(String value);

    /**
     * Expands properties in a given hash map. Returns a cloned instance with all property values expanded.
     * @param properties Properties to expand.
     * @return Expanded properties.
     */
    LinkedHashMap<String, String> expandProperties(LinkedHashMap<String, String> properties);
}
