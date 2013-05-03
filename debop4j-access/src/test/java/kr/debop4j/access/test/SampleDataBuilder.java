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

package kr.debop4j.access.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;

/**
 * 테스트 샘플용 데이터 빌드용 기본 틀래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 10:35
 */
@Slf4j
public abstract class SampleDataBuilder {

    public static final Random rnd = new Random(new Date().getTime());

    public abstract void createSampleData();
}
