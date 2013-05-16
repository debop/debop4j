/*
 * Copyright 2011-2013 the original author or authors.
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

package kr.debop4j.core.tools;

import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.core.tools.StringToolTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오후 10:30
 */
@Slf4j
public class StringToolTest extends AbstractTest {

    @Test
    public void stringSprit() throws Exception {

        String text = "동해물과 백두산이 \t      마르고 닳도록 ";

        List<String> result = StringTool.split(text, " ");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(4);

        result = StringTool.split(text, "\t");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(2);

        result = StringTool.split(text, "A");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(1);

        result = StringTool.split(text, " ", "\t");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(4);
    }

    @Test
    public void stringSpritWithignoreCase() throws Exception {

        String text = "Hello World! Hello java^^";

        List<String> result = StringTool.split(text, true, "!");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(2);

        result = StringTool.split(text, false, "hello");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(1);

        result = StringTool.split(text, true, "hello");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(2);

        result = StringTool.split(text, true, "hello", "JAVA");
        log.debug("Result=[{}]", StringTool.listToString(result));
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
