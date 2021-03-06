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
package ai.dqo.metadata.dqohome;

import ai.dqo.metadata.definitions.rules.RuleDefinitionList;
import ai.dqo.metadata.definitions.rules.RuleDefinitionListImpl;
import ai.dqo.metadata.definitions.sensors.SensorDefinitionListImpl;
import ai.dqo.metadata.id.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Root dqo.io home model for reading and managing the definitions in the application's home (DQO_HOME).
 * Those are built in rule and sensor definitions.
 */
public class DqoHomeImpl implements DqoHome {
    private static final ChildHierarchyNodeFieldMapImpl<DqoHomeImpl> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(ChildHierarchyNodeFieldMap.empty()) {
        {
			put("sensors", o -> o.sensors);
			put("rules", o -> o.rules);
        }
    };

    @JsonIgnore
    private HierarchyId hierarchyId = HierarchyId.getRoot();
    private RuleDefinitionListImpl rules;
    private SensorDefinitionListImpl sensors;
    @JsonIgnore
    private boolean dirty;

    /**
     * Creates a default dqo home implementation.
     */
    public DqoHomeImpl() {
		this.setSensors(new SensorDefinitionListImpl());
		this.setRules(new RuleDefinitionListImpl());
    }

    /**
     * Creates a dqo.io home implementation with alternative implementations (file based) of collections.
     * @param sensors Collection of sensor definitions.
     * @param rules Collection of custom rule definitions.
     */
    public DqoHomeImpl(SensorDefinitionListImpl sensors, RuleDefinitionListImpl rules) {
		this.setSensors(sensors);
		this.setRules(rules);
    }

    /**
     * Returns a collection of sensor definitions in the user home folder.
     * @return Collection of user's sensor definitions.
     */
    public SensorDefinitionListImpl getSensors() {
        return sensors;
    }

    /**
     * Returns a collection of custom rules in the user home folder.
     * @return Collection of user's custom rules.
     */
    @Override
    public RuleDefinitionList getRules() {
        return rules;
    }

    /**
     * Changes the collection of check definitions.
     * @param sensors New collection of check definitions.
     */
    public void setSensors(SensorDefinitionListImpl sensors) {
        this.sensors = sensors;
        if (sensors != null) {
            HierarchyId childHierarchyId = new HierarchyId(this.hierarchyId, "sensors");
            sensors.setHierarchyId(childHierarchyId);
            assert FIELDS.get("sensors").apply(this).getHierarchyId().equals(childHierarchyId);
        }
    }

    /**
     * Changes the collection of custom rules.
     * @param rules New collection of custom rules.
     */
    public void setRules(RuleDefinitionListImpl rules) {
        this.rules = rules;
        if (rules != null) {
            HierarchyId childHierarchyId = new HierarchyId(this.hierarchyId, "rules");
            rules.setHierarchyId(childHierarchyId);
            assert FIELDS.get("rules").apply(this).getHierarchyId().equals(childHierarchyId);
        }
    }

    /**
     * Flushes an object to a persistent store.
     */
    @Override
    public void flush() {
        // synchronize changes back to the virtual file system
		this.getSensors().flush();
		this.getRules().flush();

		this.clearDirty(false); // children that were saved should be already not dirty, the next assert will detect forgotten instances
        assert !this.isDirty();
    }

    /**
     * Returns the hierarchy ID of this node.
     *
     * @return Hierarchy ID of this node.
     */
    @Override
    public HierarchyId getHierarchyId() {
        return this.hierarchyId;
    }

    /**
     * Replaces the hierarchy ID. A new hierarchy ID is also propagated to all child nodes.
     *
     * @param hierarchyId New hierarchy ID.
     */
    @Override
    public void setHierarchyId(HierarchyId hierarchyId) {
        assert hierarchyId != null;
        this.hierarchyId = hierarchyId;
		FIELDS.propagateHierarchyIdToChildren(this, hierarchyId);
    }

    /**
     * Returns a named child. It is a named object in an object map (column map, test map) or a field name.
     *
     * @param childName Child name.
     * @return Child node.
     */
    @Override
    public HierarchyNode getChild(Object childName) {
        return FIELDS.getFieldGetter(childName.toString()).apply(this);
    }

    /**
     * Returns an iterable that iterates over child nodes.
     *
     * @return Iterable to iterate over child nodes.
     */
    @Override
    public Iterable<HierarchyNode> children() {
        return new FieldIterable(this, FIELDS);
    }

    /**
     * Sets the dirty flag to true.
     */
    @Override
    public void setDirty() {
		this.dirty = true;
    }

    /**
     * Check if the object is dirty (has changes).
     *
     * @return True when the object is dirty and has modifications.
     */
    @Override
    public boolean isDirty() {
        if (this.dirty) {
            return true;
        }

        for(HierarchyNode child : this.children()) {
            if (child.isDirty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Clears the dirty flag (sets the dirty to false). Called after flushing or when changes should be considered as unimportant.
     * @param propagateToChildren When true, clears also the dirty status of child objects.
     */
    @Override
    public void clearDirty(boolean propagateToChildren) {
		this.dirty = false;

        if (propagateToChildren) {
            for (HierarchyNode child : this.children()) {
                child.clearDirty(true);
            }
        }
    }

    /**
     * Calls a visitor (using a visitor design pattern) that returns a result.
     *
     * @param visitor   Visitor instance.
     * @param parameter Additional parameter that will be passed back to the visitor.
     * @return Result value returned by an "accept" method of the visitor.
     */
    @Override
    public <P, R> R visit(HierarchyNodeResultVisitor<P, R> visitor, P parameter) {
        return visitor.accept(this, parameter);
    }
}
