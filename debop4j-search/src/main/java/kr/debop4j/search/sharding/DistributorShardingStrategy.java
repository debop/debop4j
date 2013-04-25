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

package kr.debop4j.search.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.hibernate.search.filter.FullTextFilterImplementor;
import org.hibernate.search.indexes.spi.IndexManager;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.store.IndexShardingStrategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

/**
 * 루씬 인덱스를 Sharing을 통해 저장하도록 해줍니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오전 11:31
 */
@Slf4j
public class DistributorShardingStrategy implements IndexShardingStrategy {

    private static final String RADIX = "distributors.";
    private DirectoryProvider<?>[] providers;
    private HashMap<String, Integer> providerIdPerDistributor;


    @Override
    public void initialize(Properties properties, IndexManager[] providers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexManager[] getIndexManagersForAllShards() {
        return new IndexManager[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexManager getIndexManagerForAddition(Class<?> entity, Serializable id, String idInString, Document document) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexManager[] getIndexManagersForDeletion(Class<?> entity, Serializable id, String idInString) {
        return new IndexManager[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexManager[] getIndexManagersForQuery(FullTextFilterImplementor[] fullTextFilters) {
        return new IndexManager[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
