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
package ai.dqo.cli.completion.completers;

import ai.dqo.cli.completion.AbstractCommandAwareCompleter;
import ai.dqo.cli.completion.completedcommands.ITableNameCommand;
import ai.dqo.cli.completion.completers.cache.CliCompleterCacheKey;
import ai.dqo.cli.completion.completers.cache.CliCompletionCache;
import ai.dqo.metadata.sources.ConnectionList;
import ai.dqo.metadata.sources.ConnectionWrapper;
import ai.dqo.metadata.sources.PhysicalTableName;
import ai.dqo.metadata.sources.TableWrapper;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContext;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContextCache;
import ai.dqo.metadata.userhome.UserHome;
import ai.dqo.utils.StaticBeanFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Command line completer that will complete the list of column names when the connection name and table name was already provided in the command line.
 */
public class ColumnNameCompleter extends AbstractCommandAwareCompleter<ITableNameCommand> {
    private final UserHomeContextCache userHomeContextCache;

    /**
     * Default constructor called by the auto-completion system.
     */
    public ColumnNameCompleter() {
        BeanFactory beanFactory = StaticBeanFactory.getBeanFactory();
		this.userHomeContextCache = beanFactory.getBean(UserHomeContextCache.class);
    }

    /**
     * Testable constructor that supports a custom user home context factory.
     * @param userHomeContextCache User home context cache.
     */
    public ColumnNameCompleter(UserHomeContextCache userHomeContextCache) {
        this.userHomeContextCache = userHomeContextCache;
    }

    /**
     * The overriding method should create an iterator using the data from the command.
     *
     * @param command User command.
     * @return List of completion candidates.
     */
    @Override
    public Iterator<String> createIterator(ITableNameCommand command) {
        if (command == null) {
            return createEmptyCompletionIterator();
        }

        String connectionName = command.getConnection();
        if (Strings.isNullOrEmpty(connectionName)) {
            return createEmptyCompletionIterator();
        }

        String schemaTableName = command.getTable();
        if (Strings.isNullOrEmpty(schemaTableName)) {
            return createEmptyCompletionIterator();
        }

        Iterator<String> cachedCompletionCandidates = CliCompletionCache.getCachedCompletionCandidates(
                new CliCompleterCacheKey(this.getClass(), connectionName, schemaTableName),
                () -> {
                    UserHomeContext userHomeContext = this.userHomeContextCache.getCachedLocalUserHome();
                    UserHome userHome = userHomeContext.getUserHome();
                    ConnectionList connections = userHome.getConnections();

                    ConnectionWrapper connectionWrapper = connections.getByObjectName(connectionName, true);
                    if (connectionWrapper == null) {
                        return createEmptyCompletionIterator();
                    }

                    PhysicalTableName physicalTableName = PhysicalTableName.fromSchemaTableFilter(schemaTableName);
                    TableWrapper tableWrapper = connectionWrapper.getTables().getByObjectName(physicalTableName, true);
                    if (tableWrapper == null) {
                        return createEmptyCompletionIterator();
                    }

                    ArrayList<String> columnNames = new ArrayList<>();
                    columnNames.addAll(tableWrapper.getSpec().getColumns().keySet());

                    return columnNames.iterator();
                });

        return cachedCompletionCandidates;
    }
}
