package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.junit.Test;

public class ATest {

    @Test
    public void calcNextIntervalMonth() {
        String startDate = "2020-01-10";
        String endDate = "2020-03-25";
        String type = "MONTH";
        System.out.println("--MONTH TEST--");
        A.calculateIntervals(startDate, endDate, type);
    }

    @Test
    public void calcNextIntervalWeek() {
        String startDate = "2020-01-26";
        String endDate = "2020-03-23";
        String type = "WEEK";
        System.out.println("--WEEK TEST--");
        A.calculateIntervals(startDate, endDate, type);
    }

    @Test
    public void calcNextIntervalYear() {
        String startDate = "2018-02-01";
        String endDate = "2021-03-01";
        String type = "YEAR";
        System.out.println("--YEAR TEST--");
        A.calculateIntervals(startDate, endDate, type);
    }

    @Test
    public void calcNextIntervalQuarter() {
        String startDate = "2018-03-11";
        String endDate = "2019-06-01";
        String type = "QUARTER";
        System.out.println("--QUARTER TEST--");
        A.calculateIntervals(startDate, endDate, type);
    }
    @Test
    public void calcNextIntervalFriday13th() {
        String startDate = "2020-01-01";
        String endDate = "2020-03-15";
        String type = "FRIDAY_THE_13TH";
        System.out.println("--FRIDAY_THE_13TH TEST--");
        A.calculateIntervals(startDate, endDate, type);
    }

    @Test
    public void testInput(){
        String interval = "2020-01-26 2020-03-23";
        String startDate = interval.substring(0,10);
        String endDate =  interval.substring(11,21);
        System.out.println(startDate);
        System.out.println(endDate);
    }
}
