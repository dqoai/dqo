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

import ai.dqo.BaseTest;
import ai.dqo.metadata.sources.TableSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractSpecTests extends BaseTest {
    private AbstractSpec sut;

    @Override
    @BeforeEach
    protected void setUp() throws Throwable {
        super.setUp();
		this.sut = new TableSpec();
    }

    @Test
    void isDirty_whenStart_thenIsFalse() {
        Assertions.assertFalse(this.sut.isDirty());
    }

    @Test
    void clearDirty_whenClearDone_thenIsDirtyIsFalse() {
		this.sut.setDirty();
        Assertions.assertTrue(this.sut.isDirty());
		this.sut.clearDirty(true);
        Assertions.assertFalse(this.sut.isDirty());
    }

    @Test
    void setDirtyIf_whenTruValueDone_thenIsDirtyIsTrue() {
		this.sut.setDirtyIf(false);
        Assertions.assertFalse(this.sut.isDirty());
		this.sut.setDirtyIf(true);
        Assertions.assertTrue(this.sut.isDirty());
    }
}
