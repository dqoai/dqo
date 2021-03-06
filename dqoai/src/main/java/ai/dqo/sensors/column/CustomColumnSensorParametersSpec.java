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
package ai.dqo.sensors.column;

import ai.dqo.metadata.id.ChildHierarchyNodeFieldMap;
import ai.dqo.metadata.id.ChildHierarchyNodeFieldMapImpl;
import ai.dqo.sensors.AbstractSensorParametersSpec;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import org.apache.parquet.Strings;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Custom column level sensor that accepts a name of a sensor to execute.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class CustomColumnSensorParametersSpec extends AbstractColumnSensorParametersSpec {
    public static final ChildHierarchyNodeFieldMapImpl<CustomColumnSensorParametersSpec> FIELDS = new ChildHierarchyNodeFieldMapImpl<>(AbstractColumnSensorParametersSpec.FIELDS) {
        {
        }
    };

    @JsonPropertyDescription("Sensor definition as a folder path inside the user home sensor's folder to the folder with the right sensor.")
    private String sensorDefinitionPath;

    @JsonPropertyDescription("Dictionary of additional parameters (key / value pairs) that are passed to the sensor and may be used in the Jinja2 template.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private LinkedHashMap<String, Object> originalParams = new LinkedHashMap<>(); // used to perform comparison in the isDirty check

    /**
     * Sensor name as a path to the sensor.
     * @return Sensor name.
     */
    public String getSensorDefinitionPath() {
        return sensorDefinitionPath;
    }

    /**
     * Sets the sensor name as a path to the definition.
     * @param sensorDefinitionPath Sensor name.
     */
    public void setSensorDefinitionPath(String sensorDefinitionPath) {
		this.setDirtyIf(!Objects.equals(this.sensorDefinitionPath, sensorDefinitionPath));
        this.sensorDefinitionPath = sensorDefinitionPath;
    }

    /**
     * Returns a key/value map of additional sensor parameters.
     * @return Key/value dictionary of additional parameters.
     */
    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    /**
     * Sets a dictionary of additional sensor parameters.
     * @param params Key/value dictionary with extra parameters.
     */
    public void setParams(LinkedHashMap<String, Object> params) {
		setDirtyIf(!Objects.equals(this.params, params));
        this.params = params;
		this.originalParams = (LinkedHashMap<String, Object>) params.clone();
    }

    /**
     * Check if the object is dirty (has changes).
     *
     * @return True when the object is dirty and has modifications.
     */
    @Override
    public boolean isDirty() {
        return super.isDirty() || !Objects.equals(this.params, this.originalParams);
    }

    /**
     * Clears the dirty flag (sets the dirty to false). Called after flushing or when changes should be considered as unimportant.
     * @param propagateToChildren When true, clears also the dirty status of child objects.
     */
    @Override
    public void clearDirty(boolean propagateToChildren) {
        super.clearDirty(propagateToChildren);
		this.originalParams = (LinkedHashMap<String, Object>) this.params.clone();
    }

    /**
     * Creates and returns a copy of this object.
     */
    @Override
    public AbstractSensorParametersSpec clone() {
        CustomColumnSensorParametersSpec cloned = (CustomColumnSensorParametersSpec)super.clone();
        if (cloned.params != null) {
            cloned.params = (LinkedHashMap<String, Object>)cloned.params.clone();
        }
        if (cloned.originalParams != null) {
            cloned.originalParams = (LinkedHashMap<String, Object>)cloned.originalParams.clone();
        }

        return cloned;
    }

    /**
     * Returns the child map on the spec class with all fields.
     *
     * @return Return the field map.
     */
    @Override
    protected ChildHierarchyNodeFieldMap getChildMap() {
        return FIELDS;
    }

    /**
     * Returns the sensor definition name. This is the folder name that keeps the sensor definition files.
     *
     * @return Sensor definition name.
     */
    @Override
    public String getSensorDefinitionName() {
        return this.sensorDefinitionPath;
    }

    /**
     * This method should be overriden in derived classes and should check if there are any simple fields (String, integer, double, etc)
     * that are not HierarchyNodes (they are analyzed by the hierarchy tree engine).
     * This method should return true if there is at least one field that must be serialized to YAML.
     * It may return false only if:
     * - the parameter specification class has no custom fields (parameters are not configurable)
     * - there are some fields, but they are all nulls, so not a single field would be serialized.
     * The purpose of this method is to avoid serialization of the parameters as just "parameters: " yaml, without nested
     * fields because such a YAML is just invalid.
     *
     * @return True when the parameters spec must be serialized to YAML because it has some non-null simple fields,
     * false when serialization of the parameters may lead to writing an empty "parameters: " entry in YAML.
     */
    @Override
    public boolean hasNonNullSimpleFields() {
        return !Strings.isNullOrEmpty(this.sensorDefinitionPath) ||
                (this.params != null && this.params.size() > 0);
    }
}
