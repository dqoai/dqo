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

import ai.dqo.cli.commands.cloud.impl.CloudLoginService;
import ai.dqo.cli.exceptions.CliRequiredParameterMissingException;
import ai.dqo.cli.terminal.TerminalReader;
import ai.dqo.cli.terminal.TerminalWriter;
import ai.dqo.core.dqocloud.apikey.DqoCloudApiKey;
import ai.dqo.core.dqocloud.apikey.DqoCloudApiKeyProvider;
import ai.dqo.core.dqocloud.synchronization.DqoCloudSynchronizationService;
import ai.dqo.core.filesystem.filesystemservice.contract.DqoRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service called by "cloud sync" CLI commands to synchronize the data with DQO Cloud.
 */
@Service
public class CloudSynchronizationServiceImpl implements CloudSynchronizationService {
    private DqoCloudSynchronizationService dqoCloudSynchronizationService;
    private DqoCloudApiKeyProvider apiKeyProvider;
    private CloudLoginService cloudLoginService;
    private TerminalReader terminalReader;
    private TerminalWriter terminalWriter;

    /**
     * Default injection constructor.
     * @param dqoCloudSynchronizationService Cloud synchronization service.
     * @param apiKeyProvider Api key provider - used to check if the user logged in to DQO Cloud.
     * @param cloudLoginService Cloud login service - used to log in.
     * @param terminalReader Terminal reader.
     * @param terminalWriter Terminal writer to write the results.
     */
    @Autowired
    public CloudSynchronizationServiceImpl(
            DqoCloudSynchronizationService dqoCloudSynchronizationService,
            DqoCloudApiKeyProvider apiKeyProvider,
            CloudLoginService cloudLoginService,
            TerminalReader terminalReader,
            TerminalWriter terminalWriter) {
        this.dqoCloudSynchronizationService = dqoCloudSynchronizationService;
        this.apiKeyProvider = apiKeyProvider;
        this.cloudLoginService = cloudLoginService;
        this.terminalReader = terminalReader;
        this.terminalWriter = terminalWriter;
    }

    /**
     * Synchronize a folder type to/from DQO Cloud.
     * @param rootType Root type.
     * @param reportFiles When true, files are reported.
     * @param headlessMode The application was started in a headless mode and should not bother the user with questions (prompts).
     * @return 0 when success, -1 when an error.
     */
    public int synchronizeRoot(DqoRoot rootType, boolean reportFiles, boolean headlessMode) {
        DqoCloudApiKey apiKey = this.apiKeyProvider.getApiKey();
        if (apiKey == null) {
            // the api key is missing

            if (headlessMode) {
                throw new CliRequiredParameterMissingException("API Key is missing, please run \"cloud login\" to configure your DQO Cloud API KEY");
            }

            Boolean loginMe = this.terminalReader.promptBoolean("DQO Cloud API Key is missing, do you want to log in or register to DOQ Cloud?",
                    true, true);
            if (loginMe) {
                if (!this.cloudLoginService.logInToDqoCloud()) {
                    return -2;
                }
            } else {
                return -1;
            }
        }

        CliFileSystemSynchronizationListener synchronizationListener = new CliFileSystemSynchronizationListener(rootType, reportFiles, terminalWriter);
        this.dqoCloudSynchronizationService.synchronizeFolder(rootType, synchronizationListener);

        this.terminalWriter.writeLine(rootType.toString() + " synchronization between local DQO User Home and DQO Cloud finished.\n");

        return 0;
    }
}
