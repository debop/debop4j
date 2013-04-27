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

package org.apache.lucene.analysis.kr.utils;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.kr.morph.MorphException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 동의어 분석을 수행합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 27. 오전 12:31
 */
@Slf4j
public class SynonymUtil {

    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private static SetMultimap<String, String> synonymMap;
    private static final Set<String> EMPTY_SET = new HashSet<String>();

    static {
        try {
            synonymMap = buildSynonymMap();
        } catch (Exception e) {
            log.error("동의어 사전을 로드하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 지정한 단어의 유사어가 있으면, 모든 유사어를 반환합니다.
     *
     * @throws MorphException
     */
    public static Set<String> getSynonym(String word) throws MorphException {
        if (isTraceEnabled)
            log.trace("동의어를 찾습니다... word=[{}]", word);

        for (String key : synonymMap.keySet()) {
            Set<String> synonyms = synonymMap.get(key);
            if (key.equals(word) || synonyms.contains(word)) {
                if (isTraceEnabled)
                    log.trace("동의어를 찾았습니다. word=[{}], synonyms=[{}]", word, StringUtil.join(synonyms, ","));
                return synonyms;
            }
        }
        if (isTraceEnabled)
            log.trace("동의어가 없습니다.");

        return EMPTY_SET;
    }

    /**
     * 유사어 사전에서 정보를 로드하여, 유사어 맵을 빌드합니다.
     *
     * @throws MorphException
     */
    private static SetMultimap<String, String> buildSynonymMap() throws MorphException {

        final String filename = KoreanEnv.getInstance().getValue(KoreanEnv.FILE_SYNONYM);
        log.info("동의어 사전에서 동의어 정보를 로드합니다... filename=[{}]", filename);

        SetMultimap<String, String> mmap = TreeMultimap.create();
        try {
            List<String> lines = FileUtil.readLines(filename, "UTF-8");
            for (String line : lines) {
                String[] words = StringUtils.split(line, ",");
                if (words != null && words.length > 1) {
                    mmap.putAll(words[0], Arrays.asList(words));
                    if (isTraceEnabled)
                        log.trace("동의어를 추가합니다. words=[{}]", StringTool.listToString(words));
                }
            }
            log.info("동의어 사전을 빌드했습니다. 라인수=[{}], 동의어수=[{}]", lines.size(), mmap.values().size());
        } catch (IOException e) {
            throw new MorphException(e);
        }
        return mmap;
    }
}
