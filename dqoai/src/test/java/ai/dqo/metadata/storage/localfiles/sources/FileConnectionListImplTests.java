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
package ai.dqo.metadata.storage.localfiles.sources;

import ai.dqo.BaseTest;
import ai.dqo.metadata.sources.ConnectionList;
import ai.dqo.metadata.sources.ConnectionSpec;
import ai.dqo.metadata.sources.ConnectionWrapper;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContext;
import ai.dqo.metadata.storage.localfiles.userhome.UserHomeContextObjectMother;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;

@SpringBootTest
public class FileConnectionListImplTests extends BaseTest {
    private FileConnectionListImpl sut;
    private UserHomeContext homeContext;

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
		homeContext = UserHomeContextObjectMother.createTemporaryFileHomeContext(true);
		this.sut = (FileConnectionListImpl) homeContext.getUserHome().getConnections();
    }

    @Test
    void createAndAddNew_whenNewSourceAddedAndFlushed_thenIsSaved() {
        ConnectionWrapper wrapper = this.sut.createAndAddNew("src1");
        ConnectionSpec model = wrapper.getSpec();
        model.setUrl("url");
		homeContext.flush();

        UserHomeContext homeContext2 = UserHomeContextObjectMother.createTemporaryFileHomeContext(false);
        ConnectionList sources2 = homeContext2.getUserHome().getConnections();
        ConnectionWrapper wrapper2 = sources2.getByObjectName("src1", true);
        Assertions.assertEquals("url", wrapper2.getSpec().getUrl());
    }

    @Test
    void createAndAddNew_whenNewSourceAddedInSubFolderAndFlushed_thenIsSaved() {
        ConnectionWrapper wrapper = this.sut.createAndAddNew("area/src1");
        ConnectionSpec model = wrapper.getSpec();
        model.setUrl("url");
		homeContext.flush();

        UserHomeContext homeContext2 = UserHomeContextObjectMother.createTemporaryFileHomeContext(false);
        ConnectionList sources2 = homeContext2.getUserHome().getConnections();
        ConnectionWrapper wrapper2 = sources2.getByObjectName("area/src1", true);
        Assertions.assertEquals("url", wrapper2.getSpec().getUrl());
    }

    @Test
    void flush_whenExistingSourceLoadedModifiedAndFlushed_thenIsSaved() {
        ConnectionWrapper wrapper = this.sut.createAndAddNew("src1");
        ConnectionSpec model = wrapper.getSpec();
        model.setUrl("url");
		homeContext.flush();

        UserHomeContext homeContext2 = UserHomeContextObjectMother.createTemporaryFileHomeContext(false);
        ConnectionList sources2 = homeContext2.getUserHome().getConnections();
        ConnectionWrapper wrapper2 = sources2.getByObjectName("src1", true);
        wrapper2.getSpec().setUrl("newurl");
        homeContext2.flush();

        UserHomeContext homeContext3 = UserHomeContextObjectMother.createTemporaryFileHomeContext(false);
        ConnectionList sources3 = homeContext3.getUserHome().getConnections();
        ConnectionWrapper wrapper3 = sources3.getByObjectName("src1", true);
        Assertions.assertEquals("newurl", wrapper3.getSpec().getUrl());
    }

    @Test
    void iterator_whenConnectionAdded_thenReturnsConnection() {
        ConnectionWrapper wrapper = this.sut.createAndAddNew("src3");
        ConnectionSpec spec = wrapper.getSpec();
        spec.setUrl("url");
		homeContext.flush();

        UserHomeContext homeContext2 = UserHomeContextObjectMother.createTemporaryFileHomeContext(false);
        ConnectionList sut2 = homeContext2.getUserHome().getConnections();
        Iterator<ConnectionWrapper> iterator = sut2.iterator();
        Assertions.assertTrue(iterator.hasNext());
        ConnectionWrapper wrapperLoaded = iterator.next();
        Assertions.assertNotNull(wrapperLoaded);
        Assertions.assertEquals("url", wrapperLoaded.getSpec().getUrl());
        Assertions.assertFalse(iterator.hasNext());
    }
}
