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
package ai.dqo.cli.commands.check.impl;

import ai.dqo.cli.commands.check.CheckRunReportingMode;
import ai.dqo.cli.terminal.TerminalWriter;
import ai.dqo.execution.checks.progress.*;
import ai.dqo.utils.serialization.JsonSerializer;
import org.apache.commons.lang3.StringUtils;
import tech.tablesaw.api.Table;

/**
 * Check execution progress listener used in the CLI. Reports progress to the console.
 */
public class CliCheckExecutionProgressListener implements CheckExecutionProgressListener {
    private final TerminalWriter terminalWriter;
    private final CheckRunReportingMode mode;
    private JsonSerializer jsonSerializer;

    /**
     * Renders the header before an event.
     */
    public void renderEventHeader() {
		this.terminalWriter.writeLine(StringUtils.repeat('*', 50));
    }

    /**
     * Renders the footer after an event.
     */
    public void renderEventFooter() {
		this.terminalWriter.writeLine(StringUtils.repeat('*', 50));
		this.terminalWriter.writeLine("");
    }


    /**
     * Creates a CLI progress listener using a terminal writer to print out the results.
     * @param terminalWriter Terminal writer.
     * @param mode Reporting mode.
     * @param jsonSerializer Json serializer.
     */
    public CliCheckExecutionProgressListener(TerminalWriter terminalWriter, CheckRunReportingMode mode, JsonSerializer jsonSerializer) {
        this.terminalWriter = terminalWriter;
        this.mode = mode;
        this.jsonSerializer = jsonSerializer;
    }

    /**
     * Called before checks are started on a target table.
     *
     * @param event Log event.
     */
    @Override
    public void onExecuteChecksOnTableStart(ExecuteChecksOnTableStartEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String connectionName = event.getConnectionWrapper().getName();
            String tableName = event.getTargetTable().getTarget().toPhysicalTableName().toString();
			this.terminalWriter.writeLine(String.format("Executing quality checks on table %s from connection %s", tableName, connectionName));
			renderEventFooter();
        }
    }

    /**
     * Called before a sensor is executed for a single check. The check (and sensor) is identified in the <code>sensorRunParameters</code>.
     *
     * @param event Log event.
     */
    @Override
    public void onExecutingSensor(ExecutingSensorEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String tableName = event.getTableSpec().getTarget().toPhysicalTableName().toString();
            String checkName = event.getSensorRunParameters().getCheckHierarchyId().getLast().toString();
            String sensorDefinitionName = event.getSensorRunParameters().getSensorParameters().getSensorDefinitionName();
			this.terminalWriter.writeLine(String.format("Executing a sensor for a check %s on the table %s using a sensor definition %s",
                    checkName, tableName, sensorDefinitionName));
			renderEventFooter();
        }
    }

    /**
     * Called after a sensor was executed and returned raw (not normalized) results.
     *
     * @param event Log event.
     */
    @Override
    public void onSensorExecuted(SensorExecutedEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String tableName = event.getTableSpec().getTarget().toPhysicalTableName().toString();
            String checkName = event.getSensorRunParameters().getCheckHierarchyId().getLast().toString();
            String sensorDefinitionName = event.getSensorRunParameters().getSensorParameters().getSensorDefinitionName();
            Table resultTable = event.getSensorResult().getResultTable();
            int sensorResultCount = resultTable.rowCount();
			this.terminalWriter.writeLine(String.format("Finished executing a sensor for a check %s on the table %s using a sensor definition %s, sensor result count: %d",
                    checkName, tableName, sensorDefinitionName, sensorResultCount));
			this.terminalWriter.writeLine("");

			this.terminalWriter.writeLine("Results returned by the sensor:");
            Table tableSample = (sensorResultCount > 10) ? resultTable.first(10) : resultTable;
			this.terminalWriter.writeTable(tableSample, true);

			renderEventFooter();
        }
    }

    /**
     * Called after sensor results returned from the sensor were normalized to a standard tabular format.
     *
     * @param event Log event.
     */
    @Override
    public void onSensorResultsNormalized(SensorResultsNormalizedEvent event) {

    }

    /**
     * Called after data quality rules were executed for all rows of normalized sensor results.
     *
     * @param event Log event.
     */
    @Override
    public void onRulesExecuted(RulesExecutedEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String tableName = event.getTableSpec().getTarget().toPhysicalTableName().toString();
            String checkName = event.getSensorRunParameters().getCheckHierarchyId().getLast().toString();
            Table ruleResultsTable = event.getRuleEvaluationResult().getRuleResultsTable();
            int evaluatedRulesCount = ruleResultsTable.rowCount();
			this.terminalWriter.writeLine(String.format("Finished executing rules (thresholds) for a check %s on the table %s, verified rules count: %d",
                    checkName, tableName, evaluatedRulesCount));

			this.terminalWriter.writeLine("");
			this.terminalWriter.writeLine("Rule evaluation results:");
            Table tableSample = (evaluatedRulesCount > 10) ? ruleResultsTable.first(10) : ruleResultsTable.copy();
            tableSample.removeColumnsWithMissingValues();
			this.terminalWriter.writeTable(tableSample, true);

			renderEventFooter();
        }
    }

    /**
     * Called before the sensor results are saved for later use (they may be used later for time series calculation).
     *
     * @param event Log event.
     */
    @Override
    public void onSavingSensorResults(SavingSensorResultsEvent event) {

    }

    /**
     * Called before rule evaluation results are saved.
     *
     * @param event Log event.
     */
    @Override
    public void onSavingRuleEvaluationResults(SavingRuleEvaluationResults event) {

    }

    /**
     * Called after all checks for a table were executed, rules evaluated and the results written which ends the check execution for that table (no more work).
     *
     * @param event Log event.
     */
    @Override
    public void onTableChecksProcessingFinished(TableChecksProcessingFinished event) {

    }

    /**
     * Called before SQL template is expanded (rendered).
     *
     * @param event Log event.
     */
    @Override
    public void onBeforeSqlTemplateRender(BeforeSqlTemplateRenderEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            //String tableName = event.getInputDto().getParameters().getTable().getTarget().toPhysicalTableName().toString();
            //String sensorDefinitionPath = event.getInputDto().getParameters().getSensorDefinition().getHierarchyId().toString();
            String templateText = event.getInputDto().getTemplateText();
            String templateHomePath = event.getInputDto().getTemplateHomePath();
			this.terminalWriter.writeLine(String.format("Calling Jinja2 rendering template %s", templateHomePath));
            if (templateText != null) {
				this.terminalWriter.writeLine("The following template will be filled:");
				this.terminalWriter.writeLine(templateText);
            }

            String serializedParameters = this.jsonSerializer.serialize(event.getInputDto());
            this.terminalWriter.writeLine("JSON parameters sent to the Jinja2 template:");
            this.terminalWriter.writeLine(serializedParameters);

            renderEventFooter();
        }
    }

    /**
     * Called after an SQL template was rendered from a Jinja2 template.
     *
     * @param event Log event.
     */
    @Override
    public void onSqlTemplateRendered(SqlTemplateRenderedRendered event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String renderedTemplate = event.getOutput().getTemplate();
            String errorMessage = event.getOutput().getError();
            if (errorMessage != null) {
				this.terminalWriter.writeLine(String.format("Failed to render a Jinja2 sensor template, error: %s", errorMessage));
            } else {
				this.terminalWriter.writeLine("Jinja2 engine has rendered the following template:");
				this.terminalWriter.writeLine(renderedTemplate);
            }
			renderEventFooter();
        }
    }

    /**
     * Called before a sensor SQL is executed on a connection.
     *
     * @param event Log event.
     */
    @Override
    public void onExecutingSqlOnConnection(ExecutingSqlOnConnectionEvent event) {
        if (this.mode == CheckRunReportingMode.debug) {
			renderEventHeader();
            String connectionName = event.getConnectionSpec().getConnectionName();
            String providerType = event.getConnectionSpec().getProviderType().name();
            String sql = event.getRenderedSql();
			this.terminalWriter.writeLine(String.format("Executing SQL on connection %s (%s)", connectionName, providerType));
			this.terminalWriter.writeLine("SQL to be executed on the connection:");
			this.terminalWriter.writeLine(sql);
			renderEventFooter();
        }
    }
}
