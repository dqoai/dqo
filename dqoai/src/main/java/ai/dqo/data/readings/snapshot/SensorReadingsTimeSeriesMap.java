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

import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary of identified time series in the historic results.
 */
public class SensorReadingsTimeSeriesMap {
    private final Map<SensorReadingsTimeSeriesKey, SensorReadingsTimeSeriesData> entries = new HashMap<>();

    /**
     * Returns a known time series for the given key or null when no historic data for this time series is present.
     * @param key Time series key.
     * @return Time series data or null.
     */
    public SensorReadingsTimeSeriesData findTimeSeriesData(SensorReadingsTimeSeriesKey key) {
        return this.entries.get(key);
    }

    /**
     * Returns a known time series for the given key (check and dimension) or null when no historic data for this time series is present.
     * @param checkHashId Check hash code id.
     * @param dimensionId Dimension hash code id.
     * @return Time series data or null.
     */
    public SensorReadingsTimeSeriesData findTimeSeriesData(long checkHashId, long dimensionId) {
        return this.entries.get(new SensorReadingsTimeSeriesKey(checkHashId, dimensionId));
    }

    /**
     * Adds a time series object to the dictionary.
     * @param timeSeries Time series object.
     */
    public void add(SensorReadingsTimeSeriesData timeSeries) {
		this.entries.put(timeSeries.getKey(), timeSeries);
    }
}
