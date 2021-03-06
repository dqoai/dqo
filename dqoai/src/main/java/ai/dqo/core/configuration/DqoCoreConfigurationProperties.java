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
package ai.dqo.core.configuration;

import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration POJO with the configuration for the dqo core.
 */
@Configuration
@ConfigurationProperties(prefix = "dqo.core")
@EqualsAndHashCode(callSuper = false)
public class DqoCoreConfigurationProperties {
    private boolean printStackTrace;

    /**
     * Prints the stack trace.
     * @return Stack trace is printed when a commands fails.
     */
    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    /**
     * Enables printing the stack trace when a command fails.
     * @param printStackTrace Print stack trace.
     */
    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }
}
