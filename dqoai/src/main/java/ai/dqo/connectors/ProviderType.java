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
package ai.dqo.connectors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  Data source provider type (dialect type).
 *  We will use lower case names to avoid issues with parsing, even if the enum names are not named following the Java naming convention.
 */
public enum ProviderType {
    @JsonProperty("bigquery")
    bigquery,

    @JsonProperty("snowflake")
    snowflake,

    // TODO: add more connectors
}
