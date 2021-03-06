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
package ai.dqo.metadata.storage.localfiles;

/**
 * File names used for the spec yaml files.
 */
public final class SpecFileNames {
    /**
     * Data source (connection) spec file name.
     */
    public static final String CONNECTION_SPEC_FILE_NAME_YAML = "connection.dqoconnection.yaml";

    /**
     * Data quality sensor definition file name.
     */
    public static final String SENSOR_SPEC_FILE_NAME_YAML = "sensordefinition.dqosensor.yaml";

    /**
     * Table spec file extension.
     */
    public static final String TABLE_SPEC_FILE_EXT_YAML = ".dqotable.yaml";

    /**
     * File index spec file extension.
     */
    public static final String FILE_INDEX_SPEC_FILE_EXT_JSON = ".dqofidx.json";

    /**
     * Provider specific sensor definition file extension.
     */
    public static final String PROVIDER_SENSOR_SPEC_FILE_EXT_YAML = ".dqoprovidersensor.yaml";

    /**
     * Provider specific sensor definition file extension.
     */
    public static final String PROVIDER_SENSOR_SQL_TEMPLATE_EXT = ".sql.jinja2";

    /**
     * Custom rule definition file extension.
     */
    public static final String CUSTOM_RULE_SPEC_FILE_EXT_YAML = ".dqorule.yaml";

    /**
     * Custom module file extension (.py).
     */
    public static final String CUSTOM_RULE_PYTHON_MODULE_FILE_EXT_PY = ".py";

    /**
     * Custom settings file.
     */
    public static final String SETTINGS_SPEC_FILE_NAME_YAML = ".localsettings.dqosettings.yaml";
}
