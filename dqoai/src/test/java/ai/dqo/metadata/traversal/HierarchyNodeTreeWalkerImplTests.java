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
package ai.dqo.metadata.traversal;

import ai.dqo.BaseTest;
import ai.dqo.metadata.id.HierarchyNode;
import ai.dqo.metadata.search.AbstractSearchVisitor;
import ai.dqo.metadata.sources.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HierarchyNodeTreeWalkerImplTests extends BaseTest {
    public HierarchyNodeTreeWalkerImpl sut;

    /**
     * Called before each test.
     * This method should be overridden in derived super classes (test classes), but remember to add {@link BeforeEach} annotation in a derived test class. JUnit5 demands it.
     *
     * @throws Throwable
     */
    @Override
    @BeforeEach
    protected void setUp() throws Throwable {
        super.setUp();
		this.sut = new HierarchyNodeTreeWalkerImpl();
    }

    @Test
    void traverseHierarchyNodeTree_whenTraverseAll_thenCapturesAllChildren() {
        ColumnSpecMap columns = new ColumnSpecMap();
        columns.put("col1", new ColumnSpec());
        columns.put("col2", new ColumnSpec());

        ArrayList<HierarchyNode> foundNodes = new ArrayList<>();
        VisitAllVisitor visitor = new VisitAllVisitor();
		this.sut.traverseHierarchyNodeTree(columns, node -> node.visit(visitor, foundNodes));

        Assertions.assertEquals(2, foundNodes.size());
        Assertions.assertTrue(foundNodes.contains(columns.get("col1")));
        Assertions.assertTrue(foundNodes.contains(columns.get("col2")));
    }

    @Test
    void traverseHierarchyNodeTree_whenOneNodeSelected_thenVisitsOnlyThatNode() {
        ColumnSpecMap columns = new ColumnSpecMap();
        columns.put("col1", new ColumnSpec());
        columns.put("col2", new ColumnSpec());

        ArrayList<HierarchyNode> foundNodes = new ArrayList<>();
        VisitSelectedColumnVisitor visitor = new VisitSelectedColumnVisitor(columns.get("col1"));
		this.sut.traverseHierarchyNodeTree(columns, node -> node.visit(visitor, foundNodes));

        Assertions.assertEquals(1, foundNodes.size());
        Assertions.assertTrue(foundNodes.contains(columns.get("col1")));
    }

    @Test
    void traverseHierarchyNodeTree_whenStopTraversal_thenDoesNotVisitOtherNodes() {
        ColumnSpecMap columns = new ColumnSpecMap();
        columns.put("col1", new ColumnSpec());
        columns.put("col2", new ColumnSpec());

        ArrayList<HierarchyNode> foundNodes = new ArrayList<>();
        VisitStopTraversalAfterFound visitor = new VisitStopTraversalAfterFound(columns.get("col1")); // java maps are iterated in the reverse order, so we need to look at col2
		this.sut.traverseHierarchyNodeTree(columns, node -> node.visit(visitor, foundNodes));

        Assertions.assertEquals(1, foundNodes.size());
        Assertions.assertTrue(foundNodes.contains(columns.get("col1")));
    }

    @Test
    void traverseHierarchyNodeTree_whenTraversingDeepStructures_thenReturnsDeepObjects() {
        ConnectionWrapperImpl connectionWrapper = new ConnectionWrapperImpl("conn");
        TableList tables = connectionWrapper.getTables();
        TableWrapper table1 = tables.createAndAddNew(new PhysicalTableName("s1", "tab1"));
        TableWrapper table2 = tables.createAndAddNew(new PhysicalTableName("s2", "tab2"));
        ColumnSpec col1 = new ColumnSpec();
        table1.getSpec().getColumns().put("col1", col1);
        ColumnSpec col2 = new ColumnSpec();
        table2.getSpec().getColumns().put("col2", col2);

        ArrayList<HierarchyNode> foundNodes = new ArrayList<>();
        VisitAllVisitor visitor = new VisitAllVisitor();
		this.sut.traverseHierarchyNodeTree(connectionWrapper, node -> node.visit(visitor, foundNodes));

        Assertions.assertEquals(2, foundNodes.size());
        Assertions.assertTrue(foundNodes.contains(col1));
        Assertions.assertTrue(foundNodes.contains(col2));
    }

    public class VisitAllVisitor extends AbstractSearchVisitor {
        /**
         * Accepts a column specification.
         *
         * @param columnSpec Column specification.
         * @param parameter  Target list where found hierarchy nodes should be added.
         * @return Accept's result.
         */
        @Override
        public TreeNodeTraversalResult accept(ColumnSpec columnSpec, List<HierarchyNode> parameter) {
            parameter.add(columnSpec);
            return super.accept(columnSpec, parameter);
        }
    }

    public class VisitSelectedColumnVisitor extends AbstractSearchVisitor {
        private final ColumnSpec selected;

        public VisitSelectedColumnVisitor(ColumnSpec selected) {
            this.selected = selected;
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
            return TreeNodeTraversalResult.traverseChildNode(selected);
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
            parameter.add(columnSpec);
            return super.accept(columnSpec, parameter);
        }
    }

    public class VisitStopTraversalAfterFound extends AbstractSearchVisitor {
        private final ColumnSpec selected;

        public VisitStopTraversalAfterFound(ColumnSpec selected) {
            this.selected = selected;
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
            parameter.add(columnSpec);
            if (columnSpec == selected) {
                return TreeNodeTraversalResult.STOP_TRAVERSAL;
            }
            return TreeNodeTraversalResult.TRAVERSE_CHILDREN;
        }
    }
}
