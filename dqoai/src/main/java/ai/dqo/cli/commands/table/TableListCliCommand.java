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
package ai.dqo.cli.commands.table;

import ai.dqo.cli.commands.BaseCommand;
import ai.dqo.cli.commands.ICommand;
import ai.dqo.cli.commands.status.CliOperationStatus;
import ai.dqo.cli.commands.table.impl.TableService;
import ai.dqo.cli.completion.completedcommands.IConnectionNameCommand;
import ai.dqo.cli.completion.completers.ConnectionNameCompleter;
import ai.dqo.cli.completion.completers.TableNameCompleter;
import ai.dqo.cli.terminal.TerminalReader;
import ai.dqo.cli.terminal.TerminalWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;


/**
 * Cli command to list tables.
 */
@Component
@Scope("prototype")
@CommandLine.Command(name = "list", description = "List tables which match filters")
public class TableListCliCommand extends BaseCommand implements ICommand, IConnectionNameCommand {
    private final TableService tableImportService;
    private final TerminalReader terminalReader;
    private final TerminalWriter terminalWriter;

    @Autowired
    public TableListCliCommand(TerminalReader terminalReader,
							   TerminalWriter terminalWriter,
							   TableService tableImportService) {
        this.tableImportService = tableImportService;
        this.terminalReader = terminalReader;
        this.terminalWriter = terminalWriter;
    }

    @CommandLine.Option(names = {"-c", "--connection"}, description = "Connection name",
            required = false, completionCandidates = ConnectionNameCompleter.class)
    private String connectionName;

    @CommandLine.Option(names = {"-t", "--table"}, description = "Table name filter",
            required = false, completionCandidates = TableNameCompleter.class)
    private String tableName;

    /**
     * Returns the connection name.
     * @return Connection name.
     */
    public String getConnection() {
        return connectionName;
    }

    /**
     * Sets the connection name.
     * @param connectionName Connection name.
     */
    public void setConnection(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * Returns the table name filter.
     * @return Table name filter.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name filter.
     * @param tableName Table name filter.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {

        CliOperationStatus cliOperationStatus = tableImportService.listTables(this.connectionName, this.tableName);
        if (cliOperationStatus.isSuccess()) {
            this.terminalWriter.writeTable(cliOperationStatus.getTable(), true);
            return 0;
        } else {
            this.terminalWriter.writeLine(cliOperationStatus.getMessage());
            return -1;
        }
    }
}
