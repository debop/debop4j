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

package kr.debop4j.core.guava;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * kr.debop4j.core.guava.OrderingTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 22.
 */
@Slf4j
@SuppressWarnings( "unchecked" )
public class OrderingTest extends AbstractTest {

    private static final Comparator<Employee> yearsComparator =
            new Comparator<Employee>() {
                @Override
                public int compare(Employee o1, Employee o2) {
                    return (o1.getYearsOfService() - o2.getYearsOfService());
                }
            };
    private static final Comparator<Employee> idComparator =
            new Comparator<Employee>() {
                @Override
                public int compare(Employee o1, Employee o2) {
                    return (o1.getId() - o2.getId());
                }
            };

    @Test
    public void orderingTest() {
        Employee anakinSk = new Employee(4, "Anakin Skywalker", 4);
        Employee darthVader = new Employee(3, "Darth Vader", 4);
        Employee hanSolo = new Employee(2, "Han Solo", 10);

        List<Employee> employeeList = newArrayList(anakinSk, hanSolo, darthVader);

        log.debug("employee list=[{}]", StringTool.listToString(employeeList));


        Ordering<Employee> orderUsingYearsComparator = Ordering.from(yearsComparator);
        List<Employee> sortedCopy = orderUsingYearsComparator.sortedCopy(employeeList);
        Assert.assertEquals(4, sortedCopy.get(0).getYearsOfService());

        Ordering<Employee> compundComparator = Ordering.compound(newArrayList(yearsComparator, idComparator));
        List<Employee> compoundSorted = compundComparator.sortedCopy(employeeList);
        Assert.assertEquals(3, compoundSorted.get(0).getId());
    }

    @Getter
    static class Employee implements Comparable<Employee> {

        private final int id;
        private final String name;
        private final int yearsOfService;

        public Employee(int id, String name, int yearsOfService) {
            this.id = id;
            this.name = name;
            this.yearsOfService = yearsOfService;
        }

        @Override
        public int compareTo(Employee o) {
            return this.getName().compareTo(o.getName());
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("years of service", yearsOfService)
                    .toString();
        }
    }
}
