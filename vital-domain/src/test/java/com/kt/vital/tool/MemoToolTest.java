package com.kt.vital.tool;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 메모 내용 중에 민감한 숫자들을 '*' 로 처리한다. 단 예약어들은 그대로 둔다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 2:10
 */
@Slf4j
public class MemoToolTest {

    private static final String actual = "iphone5 동해물과 백두산이 1234 아이폰5 내용은? 아이폰 5입니다. 증말 아이폰 5 입니까?";

    @Test
    public void convertNumberNoProhibitedWords() {
        final String expected = "iphone* 동해물과 백두산이 **** 아이폰* 내용은? 아이폰 *입니다. 증말 아이폰 * 입니까?";

        MemoTool memoTool = new MemoTool();
        String converted = memoTool.convertNumber(actual, '*', null);

        Assert.assertEquals(expected, converted);
    }

    @Test
    public void convertNumberWithProhibitedWords() {

        final String expected = "iphone5 동해물과 백두산이 **** 아이폰5 내용은? 아이폰 *입니다. 증말 아이폰 * 입니까?";

        List<String> conservedWords = Lists.newArrayList("IPhone5", "아이폰5");

        MemoTool memoTool = new MemoTool();
        String converted = memoTool.convertNumber(actual, '*', conservedWords);

        Assert.assertEquals(expected, converted);
    }

    @Test
    public void convertNumberWithProhibitedWords2() {

        final String expected = "iphone5 동해물과 백두산이 **** 아이폰5 내용은? 아이폰 5입니다. 증말 아이폰 5 입니까?";

        List<String> conservedWords = Lists.newArrayList("IPhone5", "아이폰5", "아이폰 5");

        MemoTool memoTool = new MemoTool();
        String converted = memoTool.convertNumber(actual, '*', conservedWords);

        Assert.assertEquals(expected, converted);
    }
}
