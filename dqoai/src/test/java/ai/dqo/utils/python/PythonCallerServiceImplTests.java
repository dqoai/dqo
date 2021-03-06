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
package ai.dqo.utils.python;

import ai.dqo.BaseTest;
import ai.dqo.core.configuration.DqoConfigurationProperties;
import ai.dqo.core.configuration.DqoConfigurationPropertiesObjectMother;
import ai.dqo.execution.sqltemplates.JinjaTemplateRenderInput;
import ai.dqo.execution.sqltemplates.JinjaTemplateRenderOutput;
import ai.dqo.execution.sqltemplates.JinjaTemplateRenderParameters;
import ai.dqo.utils.serialization.JsonSerializerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PythonCallerServiceImplTests extends BaseTest {
    private PythonCallerServiceImpl sut;
    private DqoConfigurationProperties configurationProperties;
    private PythonVirtualEnvService pythonVirtualEnvService;

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
		this.configurationProperties = DqoConfigurationPropertiesObjectMother.getDefaultCloned();
		this.pythonVirtualEnvService = PythonVirtualEnvServiceObjectMother.getDefault();
		this.sut = new PythonCallerServiceImpl(configurationProperties, new JsonSerializerImpl(), pythonVirtualEnvService);
    }

    @Test
    void executePythonHomeScript_whenTemplateEvaluationPythonCalledWithOneObject_thenIsExecuted() {
        JinjaTemplateRenderInput inputDto = new JinjaTemplateRenderInput();
        inputDto.setTemplateText("sample template content");
        inputDto.setParameters(new JinjaTemplateRenderParameters());
        String pythonModulePath = this.configurationProperties.getPython().getEvaluateTemplatesModule();

        JinjaTemplateRenderOutput output =
				this.sut.executePythonHomeScript(inputDto, pythonModulePath, JinjaTemplateRenderOutput.class);

        Assertions.assertEquals("sample template content", output.getTemplate());
        Assertions.assertEquals("sample template content", output.getResult());
    }

    @Test
    void executePythonHomeScript_whenTemplateEvaluationPythonCalledTwice_thenIsExecuted() {
        String pythonModulePath = this.configurationProperties.getPython().getEvaluateTemplatesModule();

        JinjaTemplateRenderInput inputDto1 = new JinjaTemplateRenderInput();
        inputDto1.setTemplateText("sample template content");
        inputDto1.setParameters(new JinjaTemplateRenderParameters());

        JinjaTemplateRenderOutput output1 =
				this.sut.executePythonHomeScript(inputDto1, pythonModulePath, JinjaTemplateRenderOutput.class);

        Assertions.assertEquals("sample template content", output1.getTemplate());
        Assertions.assertEquals("sample template content", output1.getResult());

        JinjaTemplateRenderInput inputDto2 = new JinjaTemplateRenderInput();
        inputDto2.setTemplateText("sample template content2");
        inputDto2.setParameters(new JinjaTemplateRenderParameters());

        JinjaTemplateRenderOutput output2 =
				this.sut.executePythonHomeScript(inputDto2, pythonModulePath, JinjaTemplateRenderOutput.class);

        Assertions.assertEquals("sample template content2", output2.getTemplate());
        Assertions.assertEquals("sample template content2", output2.getResult());
    }

    @Test
    void executePythonHomeScriptAndFinish_whenTemplateEvaluationPythonCalledWithTwoObjects_thenIsExecutedAndReturnsTwoResponses() {
        JinjaTemplateRenderInput inputDto1 = new JinjaTemplateRenderInput();
        inputDto1.setTemplateText("sample template content");
        inputDto1.setParameters(new JinjaTemplateRenderParameters());
        JinjaTemplateRenderInput inputDto2 = new JinjaTemplateRenderInput();
        inputDto2.setTemplateText("content2");
        inputDto2.setParameters(new JinjaTemplateRenderParameters());
        List<JinjaTemplateRenderInput> inputs = new ArrayList<JinjaTemplateRenderInput>();
        inputs.add(inputDto1);
        inputs.add(inputDto2);
        String pythonModulePath = this.configurationProperties.getPython().getEvaluateTemplatesModule();

        List<JinjaTemplateRenderOutput> outputs =
				this.sut.executePythonHomeScriptAndFinish(inputs, pythonModulePath, JinjaTemplateRenderOutput.class);

        Assertions.assertEquals(2, outputs.size());
        Assertions.assertEquals("sample template content", outputs.get(0).getTemplate());
        Assertions.assertEquals("sample template content", outputs.get(0).getResult());
        Assertions.assertEquals("content2", outputs.get(1).getTemplate());
        Assertions.assertEquals("content2", outputs.get(1).getResult());
    }
}
