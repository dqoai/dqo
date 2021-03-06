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
package ai.dqo.metadata.search;

import ai.dqo.checks.AbstractCheckSpec;
import ai.dqo.metadata.id.HierarchyNode;
import ai.dqo.metadata.sources.*;
import ai.dqo.metadata.traversal.TreeNodeTraversalResult;
import ai.dqo.sensors.AbstractSensorParametersSpec;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Visitor for {@link CheckSearchFilters} that finds the correct nodes.
 */
public class CheckSearchFiltersVisitor extends AbstractSearchVisitor {
    private final CheckSearchFilters filters;

    /**
     * Creates a visitor for the given filters.
     * @param filters Check search filters.
     */
    public CheckSearchFiltersVisitor(CheckSearchFilters filters) {
        this.filters = filters;
    }

    /**
     * Accepts a list of connections.
     *
     * @param connectionList List of connections.
     * @param parameter      Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(ConnectionList connectionList, List<HierarchyNode> parameter) {
        String connectionNameFilter = this.filters.getConnectionName();
        if (Strings.isNullOrEmpty(connectionNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        if (StringPatternComparer.isSearchPattern(connectionNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // we need to iterate anyway
        }

        // exact connection name given, let's find it
        ConnectionWrapper connectionWrapper = connectionList.getByObjectName(connectionNameFilter, true);
        if (connectionWrapper == null) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // another try, maybe the name is case-sensitive
        }

        return TreeNodeTraversalResult.traverseChildNode(connectionWrapper);
    }

    /**
     * Accepts a connection wrapper (lazy loader).
     *
     * @param connectionWrapper Connection wrapper.
     * @param parameter         Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(ConnectionWrapper connectionWrapper, List<HierarchyNode> parameter) {
        String connectionNameFilter = this.filters.getConnectionName();
        if (Strings.isNullOrEmpty(connectionNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        if (StringPatternComparer.matchSearchPattern(connectionWrapper.getName(), connectionNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        return TreeNodeTraversalResult.SKIP_CHILDREN;
    }

    /**
     * Accepts a collection of tables inside a connection.
     *
     * @param tableList Table list.
     * @param parameter Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(TableList tableList, List<HierarchyNode> parameter) {
        String schemaTableName = this.filters.getSchemaTableName();
        if (Strings.isNullOrEmpty(schemaTableName)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        PhysicalTableName physicalTableName = PhysicalTableName.fromSchemaTableFilter(schemaTableName);
        if (physicalTableName.isSearchPattern()) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // we need to iterate anyway
        }

        TableWrapper tableWrapper = tableList.getByObjectName(physicalTableName, true);
        if (tableWrapper == null) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // another try, maybe the name is case-sensitive
        }

        return TreeNodeTraversalResult.traverseChildNode(tableWrapper);
    }

    /**
     * Accepts a table wrapper (lazy loader).
     *
     * @param tableWrapper Table wrapper.
     * @param parameter    Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(TableWrapper tableWrapper, List<HierarchyNode> parameter) {
        String schemaTableName = this.filters.getSchemaTableName();
        if (Strings.isNullOrEmpty(schemaTableName)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        PhysicalTableName physicalTableName = PhysicalTableName.fromSchemaTableFilter(schemaTableName);
        if (physicalTableName.isSearchPattern()) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // we need to iterate anyway
        }

        if (tableWrapper.getPhysicalTableName().matchPattern(physicalTableName)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        return TreeNodeTraversalResult.SKIP_CHILDREN;
    }

    /**
     * Accepts a table specification.
     *
     * @param tableSpec Table specification.
     * @param parameter Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(TableSpec tableSpec, List<HierarchyNode> parameter) {
        Boolean enabledFilter = this.filters.getEnabled();
        if (enabledFilter != null) {
            if (enabledFilter && tableSpec.isDisabled()) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
            if (!enabledFilter && !tableSpec.isDisabled()) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }

            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        if (tableSpec.isDisabled()) {
            return TreeNodeTraversalResult.SKIP_CHILDREN;
        }

        return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
    }

    /**
     * Accepts a column collection (map).
     *
     * @param columnSpecMap Column collection.
     * @param parameter     Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(ColumnSpecMap columnSpecMap, List<HierarchyNode> parameter) {
        String columnNameFilter = this.filters.getColumnName();
        if (Strings.isNullOrEmpty(columnNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        if (StringPatternComparer.isSearchPattern(columnNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // we need to iterate anyway
        }

        // exact column name given, let's find it
        ColumnSpec columnSpec = columnSpecMap.get(columnNameFilter);
        if (columnSpec == null) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN; // another try, maybe the name is case-sensitive
        }

        return TreeNodeTraversalResult.traverseChildNode(columnSpec);
    }

    /**
     * Accepts a column specification.
     *
     * @param columnSpec Column specification.
     * @param parameter  Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(ColumnSpec columnSpec, List<HierarchyNode> parameter) {
        Boolean enabledFilter = this.filters.getEnabled();
        if (enabledFilter != null) {
            if (enabledFilter && columnSpec.isDisabled()) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
            if (!enabledFilter && !columnSpec.isDisabled()) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
        }

        if (columnSpec.isDisabled()) {
            return TreeNodeTraversalResult.SKIP_CHILDREN;
        }

        String columnNameFilter = this.filters.getColumnName();
        if (Strings.isNullOrEmpty(columnNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        String columnName = columnSpec.getHierarchyId().getLast().toString();
        if (StringPatternComparer.matchSearchPattern(columnName, columnNameFilter)) {
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }

        return TreeNodeTraversalResult.SKIP_CHILDREN;
    }

    /**
     * Accepts any check specification.
     *
     * @param abstractCheckSpec Data quality check specification (any).
     * @param matchingNodes     Target list where found hierarchy nodes should be added.
     * @return Accept's result.
     */
    @Override
    public TreeNodeTraversalResult accept(AbstractCheckSpec abstractCheckSpec, List<HierarchyNode> matchingNodes) {
        Boolean enabledFilter = this.filters.getEnabled();
        AbstractSensorParametersSpec sensorParameters = abstractCheckSpec.getSensorParameters();
        boolean sensorEnabled = sensorParameters != null && !sensorParameters.isDisabled();
        if (enabledFilter != null) {
            if (enabledFilter && !sensorEnabled) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
            if (!enabledFilter && sensorEnabled) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
        }

        if (!sensorEnabled) {
            return TreeNodeTraversalResult.SKIP_CHILDREN;
        }

        String checkNameFilter = this.filters.getCheckName();
        if (!Strings.isNullOrEmpty(checkNameFilter)) {
            String checkName = abstractCheckSpec.getHierarchyId().getLast().toString();
            if (!StringPatternComparer.matchSearchPattern(checkName, checkNameFilter)) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
        }

        String sensorNameFilter = this.filters.getSensorName();
        if (!Strings.isNullOrEmpty(sensorNameFilter)) {
            if (sensorParameters == null) {
                return TreeNodeTraversalResult.SKIP_CHILDREN; // sensor is not configured (has no parameters, we don't know what to run)
            }
            String sensorDefinitionName = sensorParameters.getSensorDefinitionName();
            String sensorEntryName = sensorParameters.getHierarchyId().getLast().toString();
            if (!StringPatternComparer.matchSearchPattern(sensorDefinitionName, sensorNameFilter) &&
                    !StringPatternComparer.matchSearchPattern(sensorEntryName, sensorNameFilter)) {
                return TreeNodeTraversalResult.SKIP_CHILDREN;
            }
        }

        matchingNodes.add(abstractCheckSpec);

        return TreeNodeTraversalResult.SKIP_CHILDREN; // no need to search any deeper, we have found what we were looking for
    }
}
