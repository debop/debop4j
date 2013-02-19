package kr.debop4j.data.hibernate.forTesting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Hibernate Mapping 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public class MappingInfo {

    public static MappingInfo from(Package... packages) {
        return new MappingInfo(Lists.newArrayList(packages));
    }

    public static MappingInfo fromPackageContaining(Class entityClass) {
        return new MappingInfo(Lists.newArrayList(entityClass.getPackage()));
    }

    private final List<Package> mappedPackages = Lists.newArrayList();
    private final Map<String, String> queryLanguageImports = Maps.newHashMap();

    private MappingInfo(Iterable<Package> packages) {
        if (packages != null)
            for (Package p : packages)
                mappedPackages.add(p);
    }

    public Package[] getMappedPackages() {
        Package[] arr = new Package[mappedPackages.size()];
        return mappedPackages.toArray(arr);
    }
}
