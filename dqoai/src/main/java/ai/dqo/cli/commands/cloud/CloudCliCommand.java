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
import ai.dqo.cli.commands.cloud.sync.CloudSyncCliCommand;
import ai.dqo.cli.commands.connection.ConnectionAddCliCommand;
import ai.dqo.cli.commands.connection.ConnectionListCliCommand;
import ai.dqo.cli.commands.connection.ConnectionRemoveCliCommand;
import ai.dqo.cli.commands.connection.ConnectionUpdateCliCommand;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * "cloud" 1st level cli command to connect and synchronize with DQO Cloud
 */
@Component
@Scope("prototype")
@CommandLine.Command(name = "cloud", description = "Connect and synchronize the data with DQO Cloud",
        subcommands = {
        CloudLoginCliCommand.class,
        CloudSyncCliCommand.class
})
public class CloudCliCommand extends BaseCommand {
}
