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
package ai.dqo.metadata.basespecs;

import ai.dqo.metadata.id.HierarchyId;
import ai.dqo.metadata.id.HierarchyNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dictionary of spec objects indexed by a key.
 */
public abstract class AbstractDirtyTrackingSpecMap<V extends DirtyStatus & HierarchyNode>
        extends LinkedHashMap<String, V> implements HierarchyNode {
    @JsonIgnore
    private boolean dirty;
    @JsonIgnore
    private HierarchyId hierarchyId;

    /**
     * Check if the object is dirty (has changes).
     * @return True when the object is dirty and has modifications.
     */
    public boolean isDirty() {
        if (dirty) {
            return true;
        }

        for (V value : this.values()) {
            if (value.isDirty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the dirty flag to true.
     */
    public void setDirty() {
		this.dirty = true;
    }

    /**
     * Clears the dirty flag (sets the dirty to false). Called after flushing or when changes should be considered as unimportant.
     * @param propagateToChildren When true, clears also the dirty status of child objects.
     */
    public void clearDirty(boolean propagateToChildren) {
		this.dirty = false;
        if (propagateToChildren) {
            for (V value : this.values()) {
                value.clearDirty(true);
            }
        }
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
        if (this.size() > 0) {
            for (java.util.Map.Entry<String, V> entry : this.entrySet()) {
                entry.getValue().setHierarchyId(new HierarchyId(hierarchyId, entry.getKey()));
            }
        }
    }

    /**
     * Returns a named child. It is a named object in an object map (column map, test map) or a field name.
     *
     * @param childName Child name.
     * @return Child node.
     */
    @Override
    public HierarchyNode getChild(Object childName) {
        return this.get(childName);
    }

    /**
     * Returns an iterable that iterates over child nodes.
     *
     * @return Iterable to iterate over child nodes.
     */
    @Override
    public Iterable<HierarchyNode> children() {
        return (Iterable<HierarchyNode>) this.values(); // type erasure, it is an iterable (collection) of descendants of HierarchyNode
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     */
    @Override
    public V put(String key, V value) {
		this.setDirty();
        if (this.hierarchyId != null && value != null) {
            value.setHierarchyId(new HierarchyId(this.hierarchyId, key));
        }
        return super.put(key, value);
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     */
    @Override
    public V remove(Object key) {
		this.setDirty();
        return super.remove(key);
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    @Override
    public void clear() {
		this.setDirty();
        super.clear();
    }

    @Override
    public boolean remove(Object key, Object value) {
		this.setDirty();
        return super.remove(key, value);
    }

    @Override
    public boolean replace(String key, V oldValue, V newValue) {
		this.setDirty();
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(String key, V value) {
		this.setDirty();
        return super.replace(key, value);
    }

    /**
     * Copies all of the mappings from the specified map to this map.
     * These mappings will replace any mappings that this map had for
     * any of the keys currently in the specified map.
     *
     * @param m mappings to be stored in this map
     * @throws NullPointerException if the specified map is null
     */
    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
		this.setDirty();
        super.putAll(m);
    }

    @Override
    public V putIfAbsent(String key, V value) {
		this.setDirty();
        return super.putIfAbsent(key, value);
    }
}
