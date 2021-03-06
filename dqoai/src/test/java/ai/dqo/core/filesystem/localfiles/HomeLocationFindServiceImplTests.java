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
package ai.dqo.core.filesystem.localfiles;

import ai.dqo.BaseTest;
import ai.dqo.core.configuration.DqoConfigurationProperties;
import ai.dqo.core.configuration.DqoConfigurationPropertiesObjectMother;
import ai.dqo.core.configuration.DqoUserConfigurationProperties;
import ai.dqo.core.configuration.DqoUserConfigurationPropertiesObjectMother;
import ai.dqo.metadata.storage.localfiles.HomeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class HomeLocationFindServiceImplTests extends BaseTest {
    private HomeLocationFindServiceImpl sut;
    private DqoUserConfigurationProperties userConfigurationProperties;
    private DqoConfigurationProperties dqoConfigurationProperties;

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
		userConfigurationProperties = DqoUserConfigurationPropertiesObjectMother.createConfigurationWithTemporaryUserHome(true);
		dqoConfigurationProperties = DqoConfigurationPropertiesObjectMother.getDefaultCloned();
		this.sut = new HomeLocationFindServiceImpl(userConfigurationProperties, dqoConfigurationProperties);
    }

    @Test
    void getUserHomePath_whenUserHomeConfigured_thenReturnsUserHome() {
        String userHomePath = this.sut.getUserHomePath();
        Assertions.assertNotNull(userHomePath);
        Assertions.assertTrue(Path.of(userHomePath).isAbsolute());
        Assertions.assertTrue(Files.exists(Path.of(userHomePath)));
        Assertions.assertEquals(Path.of(this.userConfigurationProperties.getHome()).toAbsolutePath().toString(), userHomePath);
    }

    @Test
    void getDqoHomePath_whenDqoPathConfigured_thenReturnsPath() {
        String dqoHomePath = this.sut.getDqoHomePath();
        Assertions.assertNotNull(dqoHomePath);
        Assertions.assertEquals(Path.of(this.dqoConfigurationProperties.getHome()).toAbsolutePath().toString(), dqoHomePath);
    }

    @Test
    void getHomePath_whenUserHome_thenReturnsUserHome() {
        String userHomePath = this.sut.getHomePath(HomeType.USER_HOME);
        Assertions.assertEquals(Path.of(this.userConfigurationProperties.getHome()).toAbsolutePath().toString(), userHomePath);
    }

    @Test
    void getHomePath_whenUDqoHome_thenReturnsDqoHome() {
        String dqoHomePath = this.sut.getHomePath(HomeType.DQO_HOME);
        Assertions.assertEquals(Path.of(this.dqoConfigurationProperties.getHome()).toAbsolutePath().toString(), dqoHomePath);
    }
}
