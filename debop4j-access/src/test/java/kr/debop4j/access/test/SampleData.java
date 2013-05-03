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

package kr.debop4j.access.test;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * kr.debop4j.access.test.SampleData
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 10:47
 */
public class SampleData {

    public static final int MaxSampleCount = 10;
    public static final int AvgSampleCount = 5;
    public static final int MinSampleCount = 2;     // 꼭 복수개여야 합니다.

    @Getter(lazy = true)
    private static final String companyCode = getCompanyCodes().get(0);
    @Getter(lazy = true)
    private static final String DepartmentCode = getDepartmentCodes().get(0);
    @Getter(lazy = true)
    private static final String EmployeeCode = getEmployeeCodes().get(0);
    @Getter(lazy = true)
    private static final String UserName = getUserNames().get(0);
    @Getter(lazy = true)
    private static final String groupCode = getGroupCodes().get(0);


    @Getter(lazy = true)
    private static final List<String> companyCodes = getCodes("CO_", MinSampleCount);
    @Getter(lazy = true)
    private static final List<String> departmentCodes = getCodes("DEPT_", AvgSampleCount);
    @Getter(lazy = true)
    private static final List<String> employeeCodes = getCodes("EMP_", MaxSampleCount);
    @Getter(lazy = true)
    private static final List<String> userNames = getCodes("USER_", MaxSampleCount);
    @Getter(lazy = true)
    private static final List<String> groupCodes = getCodes("GROUP_", AvgSampleCount);

    @Getter(lazy = true)
    private static final List<String> companyCodeValuess = getCodes("CO_CODE_", MaxSampleCount);
    @Getter(lazy = true)
    private static final List<String> productCodeValues = getCodes("PRDUCT_CODE_", MaxSampleCount);

    public static List<String> getCodes(String prefix, int count) {
        List<String> codes = Lists.newArrayList();

        for (int i = 0; i < count; i++)
            codes.add(String.format("%s%04d", prefix, count));

        return codes;
    }


    public static final String ProductCode = "Access";
    @Getter
    public static final String[] productCodes = new String[] { "Access", "DAISY", "IRIS", "PUDDING.TO", "PUDDING.CAMERA" };
}
