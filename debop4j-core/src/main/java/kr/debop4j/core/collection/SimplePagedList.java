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

package kr.debop4j.core.collection;

import lombok.Getter;

import java.util.List;

import static kr.debop4j.core.Guard.*;

/**
 * 페이징된 목록을 표현하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 13
 */
@Getter
public class SimplePagedList<E> implements IPagedList<E> {

    private static final long serialVersionUID = -5027718652421583413L;

    private final List<E> list;
    private final int pageNo;
    private final int pageSize;
    private final long itemCount;
    private final long pageCount;

    /**
     * 생성자
     *
     * @param list      컬렉션
     * @param pageNo    페이지 번호 (1부터 시작)
     * @param pageSize  페이지 크기
     * @param itemCount 전체 아이템의 크기
     */
    public SimplePagedList(List<E> list, int pageNo, int pageSize, long itemCount) {
        this.list = shouldNotBeNull(list, "list");
        this.pageNo = shouldBePositiveNumber(pageNo, "pageNo");
        this.pageSize = shouldBePositiveNumber(pageSize, "pageSize");
        this.itemCount = shouldNotBeNegativeNumber(itemCount, "itemCount");

        this.pageCount = (long) (itemCount / pageSize) + ((itemCount % pageSize) > 0 ? 1 : 0);
    }
}
