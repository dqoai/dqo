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
package ai.dqo.data.readings.snapshot;

import ai.dqo.metadata.sources.PhysicalTableName;

/**
 * Sensor reading snapshot service. Creates snapshots connected to a persistent storage.
 */
public interface SensorReadingsSnapshotFactory {
    /**
     * Creates an empty snapshot that is connected to the sensor reading storage service that will load requested months on demand.
     * @param connectionName Connection name.
     * @param physicalTableName Physical table name.
     * @return Sensor readings snapshot connected to a storage service.
     */
    SensorReadingsSnapshot createSnapshot(String connectionName, PhysicalTableName physicalTableName);
}
