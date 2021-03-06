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
package ai.dqo.bigquery.connectors;

import ai.dqo.bigquery.BaseBigQueryIntegrationTest;
import ai.dqo.connectors.bigquery.BigQueryConnectionPool;
import ai.dqo.connectors.bigquery.BigQueryConnectionPoolImpl;
import ai.dqo.connectors.bigquery.BigQueryConnectionSpecObjectMother;
import ai.dqo.metadata.sources.ConnectionSpec;
import ai.dqo.utils.BeanFactoryObjectMother;
import com.google.cloud.bigquery.BigQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BigQueryConnectionPoolImplIntegrationTests extends BaseBigQueryIntegrationTest {
    private BigQueryConnectionPoolImpl sut;

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
        BeanFactory beanFactory = BeanFactoryObjectMother.getBeanFactory();
		this.sut = (BigQueryConnectionPoolImpl) beanFactory.getBean(BigQueryConnectionPool.class);
    }

    @Test
    void getBigQueryService_whenCalledOnce_thenReturnsConnection() {
        ConnectionSpec connectionSpec = BigQueryConnectionSpecObjectMother.create();
        BigQuery bigQueryService = this.sut.getBigQueryService(connectionSpec);
        Assertions.assertNotNull(bigQueryService);
    }

    @Test
    void getBigQueryService_whenCalledAgain_thenReturnsTheSameInstance() {
        ConnectionSpec connectionSpec = BigQueryConnectionSpecObjectMother.create();
        BigQuery bigQueryService = this.sut.getBigQueryService(connectionSpec);
        Assertions.assertNotNull(bigQueryService);
        Assertions.assertSame(bigQueryService, this.sut.getBigQueryService(connectionSpec));
    }

    @Test
    void getBigQueryService_whenCalledWithQuotaProject_thenReturnsConnection() {
        ConnectionSpec connectionSpec = BigQueryConnectionSpecObjectMother.create();
        connectionSpec.getBigquery().setQuotaProjectId(connectionSpec.getBigquery().getSourceProjectId());
        BigQuery bigQueryService = this.sut.getBigQueryService(connectionSpec);
        Assertions.assertNotNull(bigQueryService);
    }
}
