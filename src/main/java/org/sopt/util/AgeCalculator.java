package org.sopt.util;

import java.time.LocalDate;
import java.time.Period;

public final class AgeCalculator {
    private AgeCalculator() {}

    public static int calculate(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears();
    }
}
