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
package ai.dqo.core.dqocloud.apikey;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of limits used in an API Key.
 */
public enum DqoCloudLimit {
    /**
     * Limit on the number of connections.
     */
    @JsonProperty("cl")
    CONNECTIONS_LIMIT,

    /**
     * Months limit - limit of the number of past months that are stored.
     */
    @JsonProperty("ml")
    MONTHS_LIMIT,

    /**
     * Tables per connections limit.
     */
    @JsonProperty("tcl")
    CONNECTION_TABLES_LIMIT
}
