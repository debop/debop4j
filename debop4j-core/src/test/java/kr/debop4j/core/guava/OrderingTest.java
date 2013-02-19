package kr.debop4j.core.guava;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import kr.debop4j.core.AbstractTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * kr.debop4j.core.guava.OrderingTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 22.
 */
@Slf4j
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

        log.debug("employee list=[{}]", employeeList);


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
