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
package ai.dqo.data.local;

import ai.dqo.core.configuration.DqoUserConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * Service that returns an absolute path to the local user home, used to access data files.
 */
@Component
public class LocalDqoUserHomePathProviderImpl implements LocalDqoUserHomePathProvider {
    private DqoUserConfigurationProperties dqoUserConfigurationProperties;

    /**
     * Default injection constructor.
     * @param dqoUserConfigurationProperties  User home configuration properties.
     */
    @Autowired
    public LocalDqoUserHomePathProviderImpl(DqoUserConfigurationProperties dqoUserConfigurationProperties) {
        this.dqoUserConfigurationProperties = dqoUserConfigurationProperties;
    }

    /**
     * Returns the absolute path to the DQO_USER_HOME folder.
     * @return Absolute path to the DQO user home folder.
     */
    public Path getLocalUserHomePath() {
        Path absolutePathToLocalUserHome = Path.of(this.dqoUserConfigurationProperties.getHome()).toAbsolutePath();
        return absolutePathToLocalUserHome;
    }
}
