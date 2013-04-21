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

package kr.debop4j.data.ogm;

import kr.debop4j.data.ogm.dao.Player;
import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.data.ogm.GridDatastoreConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 12:05
 */
@Configuration
@Slf4j
public class GridDatastoreConfiguration extends GridDatastoreConfigBase {
    @Override
    protected String getDatabaseName() {
        return "debop4j_ogm";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Player.class.getPackage().getName()
        };
    }

//    @Override
//    protected Class<?>[] getMappedEntities() {
//        return new Class<?>[] { Player.class };
//    }
}
