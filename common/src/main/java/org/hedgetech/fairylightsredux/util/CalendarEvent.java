package org.hedgetech.fairylightsredux.util;

import java.time.LocalDate;
import java.time.Month;

public record CalendarEvent(Month month, int dayStart, int dayEnd) {
    public static final CalendarEvent CHRISTMAS = new CalendarEvent(Month.DECEMBER, 24, 26);
    public static final CalendarEvent HALLOWEEN = new CalendarEvent(Month.OCTOBER, 31, 31);

    public boolean isOccurringNow() {
        final LocalDate now = LocalDate.now();
        return now.getMonth() == this.month
                && now.getDayOfMonth() >= this.dayStart
                && now.getDayOfMonth() <= this.dayEnd;
    }
}
