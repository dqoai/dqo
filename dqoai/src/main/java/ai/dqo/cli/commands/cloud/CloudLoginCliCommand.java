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
package ai.dqo.cli.commands.cloud;

import ai.dqo.cli.commands.BaseCommand;
import ai.dqo.cli.commands.ICommand;
import ai.dqo.cli.commands.cloud.impl.CloudLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * 2st level CLI command "cloud login" to log in to the DQO Cloud.
 */
@Component
@Scope("prototype")
@CommandLine.Command(name = "login", description = "Logs in or registers an account at DQO Cloud")
public class CloudLoginCliCommand extends BaseCommand implements ICommand {
    private CloudLoginService cloudLoginService;

    @Autowired
    public CloudLoginCliCommand(CloudLoginService cloudLoginService) {
        this.cloudLoginService = cloudLoginService;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        boolean success = this.cloudLoginService.logInToDqoCloud();
        return success ? 0 : -1;
    }
}
