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
package ai.dqo.core.filesystem;

/**
 * Built-in folder names used in the home folder.
 */
public final class BuiltInFolderNames {
    /**
     * Sources folder.
     */
    public static final String SOURCES = "sources";

    /**
     * Sensors folder.
     */
    public static final String SENSORS = "sensors";

    /**
     * Rules folder.
     */
    public static final String RULES = "rules";

    /**
     * Local data folder.
     */
    public static final String DATA = ".data";

    /**
     * Local index folder.
     */
    public static final String INDEX = ".index";

    /**
     * Credentials folder to store local credentials that are not stored in the repository.
     */
    public static final String CREDENTIALS = ".credentials";
}
