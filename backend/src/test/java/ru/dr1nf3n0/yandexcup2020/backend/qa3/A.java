package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class A {

    public static class LocalDateInterval {
        private LocalDate start;
        private LocalDate end;

        public LocalDateInterval(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }

        public LocalDate getStart() {
            return start;
        }

        public void setStart(LocalDate start) {
            this.start = start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public void setEnd(LocalDate end) {
            this.end = end;
        }
    }

    public static void calculateIntervals(String startDate, String endDate, String type) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate nextStart = start;
        TemporalAdjuster nextEndAdjuster;
        TemporalAdjuster nextStartAdjuster;
        switch (type) {
            case "MONTH":
                nextStartAdjuster = TemporalAdjusters.firstDayOfNextMonth();
                nextEndAdjuster = TemporalAdjusters.lastDayOfMonth();
                break;
            case "WEEK":
                nextStartAdjuster = TemporalAdjusters.next(DayOfWeek.MONDAY);
                nextEndAdjuster = TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY);
                break;
            case "YEAR":
                nextStartAdjuster = TemporalAdjusters.firstDayOfNextYear();
                nextEndAdjuster = TemporalAdjusters.lastDayOfYear();
                break;
            case "QUARTER":
                nextStartAdjuster = new TemporalAdjuster() {
                    int [] nextStartArr = new int[]{ 1,4,7,10};
                    @Override
                    public Temporal adjustInto(Temporal temporal) {
                        //01,04,07,10
                        Temporal temporal1 = temporal;
                        int currentMonthNum = temporal1.get(ChronoField.MONTH_OF_YEAR);
                        if(currentMonthNum == 1 || currentMonthNum == 4 || currentMonthNum == 7 || currentMonthNum == 10){
                            currentMonthNum++;
                        }
                        while (currentMonthNum != 1 &&
                                currentMonthNum != 4 &&
                                currentMonthNum != 7 &&
                                currentMonthNum != 10
                        ) {
                            temporal1 = temporal1.with(TemporalAdjusters.firstDayOfNextMonth());
                            currentMonthNum = temporal1.get(ChronoField.MONTH_OF_YEAR);
                        }
                        return temporal1;
                    }
                };
                nextEndAdjuster = new TemporalAdjuster() {
                    @Override
                    public Temporal adjustInto(Temporal temporal) {
                        // 03,06,09,12
                        Temporal temporal1 = temporal;
                        int currentMonthNum = temporal1.get(ChronoField.MONTH_OF_YEAR);
                        while (currentMonthNum  != 3 &&
                                currentMonthNum  != 6 &&
                                currentMonthNum  != 9 &&
                                currentMonthNum  != 12
                        ) {
                            temporal1 = temporal1.with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.lastDayOfMonth());
                            currentMonthNum = temporal1.get(ChronoField.MONTH_OF_YEAR);
                        }
                        return temporal1.with(TemporalAdjusters.lastDayOfMonth());
                    }
                };
                break;
            case "FRIDAY_THE_13TH":
                nextStartAdjuster = new TemporalAdjuster() {
                    @Override
                    public Temporal adjustInto(Temporal temporal) {
                        Temporal temporal1 = temporal;
                        while (true) {
                            temporal1 = temporal1.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                            if (temporal1.get(ChronoField.DAY_OF_WEEK) == 5 && temporal1.get(ChronoField.DAY_OF_MONTH) == 13) {
                                break;
                            }
                        }
                        return temporal1;
                    }
                };
                nextEndAdjuster = new TemporalAdjuster() {
                    @Override
                    public Temporal adjustInto(Temporal temporal) {
                        Temporal temporal1 = temporal;
                        while (true) {
                            if (temporal1.get(ChronoField.DAY_OF_WEEK) == 4 && temporal1.get(ChronoField.DAY_OF_MONTH) == 12) {
                                break;
                            }
                            temporal1 = temporal1.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
                        }
                        return temporal1;

                    }
                };

                break;
            default:
                nextStartAdjuster = null;
                nextEndAdjuster = null;
                break;

        }
        List<LocalDateInterval> result = new ArrayList<>();
        while (true) {
            LocalDate nextEnd = nextStart.with(nextEndAdjuster);
            if (nextEnd.isAfter(end) || nextEnd.isEqual(end)) {
                result.add(new LocalDateInterval(nextStart, end));
                break;
            }
            result.add(new LocalDateInterval(nextStart, nextEnd));
            nextStart = nextStart.with(nextStartAdjuster);
        }
        System.out.println(result.size());
        for (LocalDateInterval e : result) {
            System.out.println(e.getStart() + " " + e.getEnd());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String type =  scanner.nextLine();
        String interval = scanner.nextLine();
        String startDate = interval.substring(0,10);
        String endDate =  interval.substring(11,21);
        calculateIntervals(startDate,endDate,type);
    }
}
