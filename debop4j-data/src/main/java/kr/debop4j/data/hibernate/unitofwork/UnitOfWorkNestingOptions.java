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

package kr.debop4j.data.hibernate.unitofwork;

/**
 * {@link IUnitOfWork} 생성 옵션
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 29.
 */
public enum UnitOfWorkNestingOptions {

    /** 기존에 {@link IUnitOfWork} 가 있다면, 그것을 사용하고, 아니면 새로운 UnitOfWork를 생성한다. */
    ReturnExistingOrCreateUnitOfWork(0),
    /** 새로운 {@link IUnitOfWork} 인스턴스를 생성하고, 기존에 IUnitOfWork 인스턴스가 있다면, 새로 생성된 UnitOfWork를 중첩한다. */
    CreateNewOrNestUnitOfWork(1);

    private final int option;

    UnitOfWorkNestingOptions(int option) {
        this.option = option;
    }

    public final int getValue() {
        return option;
    }

    /**
     * UnitOfWorkNestingOptions 로 변환합니다.
     *
     * @param value Option 값
     * @return UnitOfWorkNestingOptions 값
     */
    public static UnitOfWorkNestingOptions valueOf(int value) {
        switch (value) {
            case 0:
                return ReturnExistingOrCreateUnitOfWork;
            case 1:
                return CreateNewOrNestUnitOfWork;
            default:
                throw new IllegalArgumentException();
        }
    }
}
