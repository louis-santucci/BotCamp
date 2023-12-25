package com.botcamp.botcamp_api.execution;

import com.botcamp.botcamp_api.repository.entity.TaskExecutionEntity;
import com.botcamp.common.request.SortingOrderParameter;
import com.botcamp.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ExecutionQueryFilter {
    String beginDateStr;
    String endDateStr;
    List<ExecutionStatus> statuses;
    List<ExecutionType> types;
    SortingOrderParameter sorting;

    public Stream<TaskExecutionEntity> filter(Stream<TaskExecutionEntity> stream) {
        Stream<TaskExecutionEntity> filteredStream = stream;
        if (beginDateStr != null) {
            filteredStream = stream
                    .filter(exec -> {
                        Date beginDate = DateUtils.localDateTimeToDate(DateUtils.stringToLocalDateTime(beginDateStr, DateUtils.formatter));
                        return exec.getUpdatedAt().after(beginDate);
                    });
        }
        if (endDateStr != null) {
            filteredStream = filteredStream
                    .filter(exec -> {
                        Date endDate = DateUtils.localDateTimeToDate(DateUtils.stringToLocalDateTime(endDateStr, DateUtils.formatter));
                        return exec.getUpdatedAt().before(endDate);
                    });
        }
        if (statuses != null && !statuses.isEmpty()) {
            filteredStream = filteredStream.filter(exec -> statuses.contains(exec.getStatus()));
        }
        if (types != null && !types.isEmpty()) {
            filteredStream = filteredStream.filter(exec -> types.contains(exec.getType()));
        }
        if (sorting != null) {
            filteredStream = (sorting == SortingOrderParameter.ASCENDING)
                    ? filteredStream.sorted()
                    : filteredStream.sorted(Comparator.reverseOrder());
        }

        return filteredStream;
    }
}
