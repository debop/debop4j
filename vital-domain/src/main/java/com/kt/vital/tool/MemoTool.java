package com.kt.vital.tool;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 메모와 관련된 Tool
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 1:18
 */
@Component
@Slf4j
public class MemoTool {

    private static final boolean isDebugEnabled = log.isDebugEnabled();

    /**
     * 지정된 문자열에서 숫자에 해당하는 Character를 cryptChar('*')로 변환합니다.
     * 단 prohibitedWorkds 에 해당하는 단어는 변환하지 않습니다.
     *
     * @param memo           변환 대상 메모
     * @param cryptChar      변환할 Charactor( '*' )
     * @param conservedWords 변환하지 말아야 할 단어
     * @return
     */
    public String convertNumber(final String memo, final Character cryptChar, List<String> conservedWords) {

        if (conservedWords == null || conservedWords.size() == 0) {
            return memo.replaceAll("\\d", cryptChar.toString());
        }
        StringBuilder builder = new StringBuilder();

        List<String> words = Lists.newArrayList();
        for (String word : conservedWords)
            words.add(word.toLowerCase());

        int length = memo.length();
        int i = 0;
        while (i < length) {
            boolean prohibited = false;
            Character c = memo.charAt(i);
            String s = c.toString().toLowerCase();
            for (String word : words) {
                if (word.startsWith(s)) {
                    String target = memo.substring(i, i + word.length());
                    if (word.equals(target.toLowerCase())) {
                        i += target.length();
                        builder.append(target);
                        prohibited = true;
                        if (log.isDebugEnabled())
                            log.debug("보전해야 할 단어를 찾았습니다. word=[{}]", target);
                        break;
                    }
                }
            }
            if (!prohibited) {
                if (Character.isDigit(c))
                    builder.append(cryptChar);
                else
                    builder.append(c);
                i++;
            }
        }

        return builder.toString();
    }
}
