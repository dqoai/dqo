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
package ai.dqo.cli.commands.cloud.sync.impl;

import ai.dqo.core.filesystem.filesystemservice.contract.DqoRoot;

/**
 * Service called by "cloud sync" CLI commands to synchronize the data with DQO Cloud.
 */
public interface CloudSynchronizationService {
    /**
     * Synchronize a folder type to/from DQO Cloud.
     * @param rootType Root type.
     * @param reportFiles When true, files are reported.
     * @param headlessMode The application was started in a headless mode and should not bother the user with questions (prompts).
     * @return 0 when success, -1 when an error.
     */
    int synchronizeRoot(DqoRoot rootType, boolean reportFiles, boolean headlessMode);
}
