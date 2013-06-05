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

package kr.debop4j.data.hibernate.forTesting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Hibernate Mapping 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19.
 */
@Slf4j
public class MappingInfo {

    /**
     * From mapping info.
     *
     * @param packages the packages
     * @return the mapping info
     */
    public static MappingInfo from(Package... packages) {
        return new MappingInfo(Lists.newArrayList(packages));
    }

    /**
     * From package containing.
     *
     * @param entityClass the entity class
     * @return the mapping info
     */
    public static MappingInfo fromPackageContaining(Class entityClass) {
        return new MappingInfo(Lists.newArrayList(entityClass.getPackage()));
    }

    private final List<Package> mappedPackages = Lists.newArrayList();
    private final Map<String, String> queryLanguageImports = Maps.newHashMap();

    private MappingInfo(Iterable<Package> packages) {
        if (packages != null)
            for (Package p : packages)
                mappedPackages.add(p);
    }

    /**
     * Get mapped packages.
     *
     * @return the package [ ]
     */
    public Package[] getMappedPackages() {
        Package[] arr = new Package[mappedPackages.size()];
        return mappedPackages.toArray(arr);
    }

    /**
     * Get mapped package names.
     *
     * @return the string [ ]
     */
    public String[] getMappedPackageNames() {
        List<String> names = Lists.newArrayList();
        for (Package pkg : mappedPackages)
            names.add(pkg.getName());
        return (String[]) names.toArray();
    }
}
