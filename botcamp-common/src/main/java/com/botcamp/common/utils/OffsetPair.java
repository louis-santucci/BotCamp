package com.botcamp.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class OffsetPair {
    private boolean isNegative;
    private Integer offsetHours;
    private Integer offsetMinutes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetPair pair = (OffsetPair) o;
        return isNegative == pair.isNegative && Objects.equals(offsetHours, pair.offsetHours) && Objects.equals(offsetMinutes, pair.offsetMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isNegative, offsetHours, offsetMinutes);
    }
}
