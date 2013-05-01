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

import com.google.common.collect.Lists;
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * {@link org.modelmapper.ModelMapper} 를 이용하여, 객체간의 정보를 매핑합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 20.
 */
@Slf4j
public final class MapperTool {

    private MapperTool() {
    }

    private static final ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration()
                .enableFieldMatching(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        if (log.isInfoEnabled())
            log.info("ModelMapper를 초기화했습니다.");
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        shouldNotBeNull(source, "source");
        shouldNotBeNull(destinationClass, "destinationClass");

        return mapper.map(source, destinationClass);
    }

    public static void map(Object source, Object destination) {
        shouldNotBeNull(source, "source");
        shouldNotBeNull(destination, "destination");

        mapper.map(source, destination);
    }

    public static <S, T> List<T> mapList(Iterable<S> sources, Class<T> destinationClass) {
        List<T> destinations = Lists.newArrayList();

        for (S source : sources) {
            destinations.add(mapper.map(source, destinationClass));
        }
        return destinations;
    }

    public static <T> Future<T> mapAsync(final Object source, final Class<T> destinationClass) {
        return AsyncTool.startNew(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return mapper.map(source, destinationClass);
            }
        });
    }

    public static <S, T> Future<List<T>> mapListAsync(final Iterable<S> sources, final Class<T> destinationClass) {

        return AsyncTool.startNew(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                List<T> destinations = Lists.newArrayList();
                for (S source : sources) {
                    destinations.add(mapper.map(source, destinationClass));
                }
                return destinations;
            }
        });
    }
}
