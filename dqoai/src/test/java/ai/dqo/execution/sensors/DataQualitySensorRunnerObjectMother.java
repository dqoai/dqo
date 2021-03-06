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
package ai.dqo.execution.sensors;

import ai.dqo.execution.CheckExecutionContext;
import ai.dqo.execution.checks.progress.CheckExecutionProgressListenerStub;
import ai.dqo.metadata.storage.localfiles.dqohome.DqoHomeContextObjectMother;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContext;
import ai.dqo.utils.BeanFactoryObjectMother;
import org.springframework.beans.factory.BeanFactory;

/**
 * Object mother for DataQualitySensorRunner.
 */
public class DataQualitySensorRunnerObjectMother {
    /**
     * Returns the dfault data quality sensor runner that runs a single sensor on a real connection.
     * @return Data quality sensor runner.
     */
    public static DataQualitySensorRunnerImpl getDefault() {
        BeanFactory beanFactory = BeanFactoryObjectMother.getBeanFactory();
        return (DataQualitySensorRunnerImpl) beanFactory.getBean(DataQualitySensorRunner.class);
    }

    /**
     * Executes a sensor given the execution context (both the user home and dqo home) and the check run parameters.
     * @param checkExecutionContext Check execution context (user home and dqo home).
     * @param sensorRunParameters Sensor run parameters.
     * @return Sensor execution result.
     */
    public static SensorExecutionResult executeSensor(CheckExecutionContext checkExecutionContext, SensorExecutionRunParameters sensorRunParameters) {
        DataQualitySensorRunnerImpl sensorRunner = getDefault();
        CheckExecutionProgressListenerStub progressListener = new CheckExecutionProgressListenerStub();
        return sensorRunner.executeSensor(checkExecutionContext, sensorRunParameters, progressListener, false);
    }

    /**
     * Executes a sensor given the user home and the sensor run parameters. Uses the default dqo home (built-in sensors) at DQO_HOME.
     * @param userHomeContext User home context.
     * @param sensorRunParameters Sensor run parameters.
     * @return Sensor execution result.
     */
    public static SensorExecutionResult executeSensor(UserHomeContext userHomeContext, SensorExecutionRunParameters sensorRunParameters) {
        CheckExecutionContext checkExecutionContext = new CheckExecutionContext(userHomeContext, DqoHomeContextObjectMother.getRealDqoHomeContext());
        return executeSensor(checkExecutionContext, sensorRunParameters);
    }
}
