package kr.debop4j.core.tools;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.google.common.collect.Lists;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * kr.nsoft.commons.tools.MapperToolTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 6.
 */
public class MapperToolTest extends AbstractTest {

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void mapTest() {
        Parent parent = getParentSample();

        ParentDTO parentDTO = MapperTool.map(parent, ParentDTO.class);

        Assert.assertNotNull(parentDTO);
        Assert.assertEquals(parent.getChildren().size(), parentDTO.getChildren().size());
        Assert.assertEquals(parent.getName(), parentDTO.getName());

        for (int i = 0; i < parent.getChildren().size(); i++) {
            Assert.assertEquals(parent.getChildren().get(i).getName(), parentDTO.getChildren().get(i).getName());
            Assert.assertEquals(parent.getChildren().get(i).getDescription(), parentDTO.getChildren().get(i).getDescription());
        }
    }

    private static Parent getParentSample() {
        Parent parent = new Parent();
        parent.setId(1);
        parent.setAge(45);
        parent.setName("배성혁");
        parent.setDescription("부모 객체입니다.");

        for (int i = 0; i < 10; i++) {
            Child child = new Child();
            child.setId(i);
            child.setName("자식-" + i);
            child.setDescription("Child description - " + i);
            child.setParent(parent);

            parent.getChildren().add(child);
        }
        return parent;
    }


    @Getter
    @Setter
    public static class Parent extends ValueObjectBase {

        private Integer id;
        private Integer age;
        private String name;
        private String description;
        private List<Child> children = Lists.newArrayList();
    }

    @Getter
    @Setter
    public static class Child extends ValueObjectBase {

        private Integer id;
        private String name;
        private String description;
        private Parent parent;
    }

    @Getter
    @Setter
    public static class ParentDTO extends ValueObjectBase {

        private Integer id;
        private Integer age;
        private String name;
        private List<ChildDTO> children = Lists.newArrayList();
    }

    @Getter
    @Setter
    public static class ChildDTO extends ValueObjectBase {

        private Integer id;
        private String name;
        private String description;
        private ParentDTO parent;
    }
}
