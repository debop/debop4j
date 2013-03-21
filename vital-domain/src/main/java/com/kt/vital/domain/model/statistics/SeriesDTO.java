package com.kt.vital.domain.model.statistics;

import com.google.common.collect.Maps;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 실시간 데이터의 통계의 시간별 추이를 보기 위한 클래스
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class SeriesDto extends ValueObjectBase {

    private static final long serialVersionUID = -6701926683055241418L;

    public SeriesDto() {}

    public SeriesDto(List<Snapshot> dataList) {
        buildSeries(dataList);
    }

    /**
     * 리얼타임으로 측정한 값들을 종류별로
     */
    private Map<String, List<SeriesItemDto>> series = Maps.newHashMap();

    /**
     * Normalized 된 엔티티 값을 도표를 그리기 쉽게 Measure 로 표현한다.
     *
     * @param dataList 측정 데이터의 배열
     */
    private void buildSeries(List<Snapshot> dataList) {

        // series 초기화
        series.clear();
        for (Snapshot data : dataList) {
            for (SnapshotItem item : data.getItems()) {
                series.put(item.getName(), new ArrayList<SeriesItemDto>());
            }
        }

        // series에 데이터 추가
        for (Snapshot data : dataList) {
            DateTime snapshotTime = data.getSnapshotTime();
            for (SnapshotItem item : data.getItems()) {
                series.get(item.getName()).add(new SeriesItemDto(snapshotTime, item.getValue()));
            }
        }
    }
}
