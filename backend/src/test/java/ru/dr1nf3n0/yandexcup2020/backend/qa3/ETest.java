package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ETest {

    @Test
    public void test1(){
        //columns numbers starts from 0, not 1
        int [] importantColNums = new int[]{
                2,1
        };
        String db [][] =new String[6][3];
        db[0] = new String[]{
                "a","b",""
        };
        db[1] = new String[]{
            "","b","c"
        };
        db[2] =new String[]{
                "a","c",""
        };
        db[3] =new String[]{
            "a","b", "d e"
        };
        db[4] =new String[]{
                "","","d e"

        };
        db[5] =new String[]{
                "a","","f"
        };

        assertEquals(3,E.calculate(importantColNums,db));
    }
}
