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

package kr.debop4j.access.model.organization;

/**
 * 조직의 종류 (회사, 부서, 그룹, 직원)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 5 오후 4:27
 */
public enum OrganizationKind {

    Company("Company"),
    Department("Department"),
    Employee("Employee"),
    Group("Group");

    private final String organizationKind;

    OrganizationKind(String organizationKind) {
        this.organizationKind = organizationKind;
    }
}
