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
package ai.dqo.execution;

import ai.dqo.metadata.storage.localfiles.dqohome.DqoHomeContextFactory;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Check execution context factory. Opens the dqo home context and the user home context.
 */
@Component
public class CheckExecutionContextFactoryImpl implements CheckExecutionContextFactory {
    private final UserHomeContextFactory userHomeContextFactory;
    private final DqoHomeContextFactory dqoHomeContextFactory;

    /**
     * Dependency injection constructor.
     * @param userHomeContextFactory User home context factory.
     * @param dqoHomeContextFactory Dqo home context factory.
     */
    @Autowired
    public CheckExecutionContextFactoryImpl(UserHomeContextFactory userHomeContextFactory,
											DqoHomeContextFactory dqoHomeContextFactory) {
        this.userHomeContextFactory = userHomeContextFactory;
        this.dqoHomeContextFactory = dqoHomeContextFactory;
    }

    /**
     * Creates a new check execution context by opening the user home context and the dqo system home context.
     * @return Check execution context.
     */
    public CheckExecutionContext create() {
        return new CheckExecutionContext(this.userHomeContextFactory.openLocalUserHome(), this.dqoHomeContextFactory.openLocalDqoHome());
    }
}
