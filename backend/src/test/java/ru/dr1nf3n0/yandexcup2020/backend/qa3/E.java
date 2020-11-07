package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import java.util.ArrayList;
import java.util.Scanner;

public class E {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] firstLine = scanner.nextLine().split("\\s");
        int rowCount = Integer.parseInt(firstLine[0]);
        int colCount = Integer.parseInt(firstLine[1]);
        String[] secondLine = scanner.nextLine().split("\\s+");
        int importantColCount = Integer.parseInt(secondLine[0]);
        int importantColNums[] = new int[importantColCount];

        for (int i = 1; i <= importantColCount; i++) {
            //transform important column nums to start from 0
            importantColNums[i - 1] = Integer.parseInt(secondLine[i]) - 1;
        }
        String db[][] = new String[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            String[] rowItems = scanner.nextLine().split("\\t");
            for (int j = 0; j < rowItems.length; j++) {
                db[i][j] = rowItems[j];
            }
        }
        int result = calculate(importantColNums, db);
        System.out.println(result);
    }


    public static boolean potentiallyEqual(String[] currentKeyRow, String[] currentRow) {
        //assuming that length of rows are equals otherwise something bad occurred during parsing
        for (int colIdx = 0; colIdx < currentRow.length; colIdx++) {
            //empty values won't be checked
            if (currentRow[colIdx].isEmpty() || currentKeyRow[colIdx].isEmpty()) {
                continue;
            }
            if (!currentRow[colIdx].equals(currentKeyRow[colIdx])) {
                return false;
            }
        }
        return true;
    }

    public static int calculate(int[] importantColNums, String db[][]) {
        ArrayList<Integer> keyRowIds = calcNonNullImportantColRowIds(db, importantColNums);
        int result = 0;
        for (int currentRowId = 0; currentRowId < db.length; currentRowId++) {
            for (Integer keyRowId : keyRowIds) {
                if (!keyRowId.equals(currentRowId)) {
                    String[] currentKeyRow = db[keyRowId];
                    String[] currentRow = db[currentRowId];
                    boolean isPotentiallyEqual = potentiallyEqual(currentKeyRow, currentRow);
                    if (isPotentiallyEqual) {
                        result++;
                    }
                } else {
                    //skip key rows in db
                    break;
                }
            }
        }
        return result;
    }

    public static ArrayList<Integer> calcNonNullImportantColRowIds(String db[][], int[] importantColNums) {
        ArrayList<Integer> keyRows = new ArrayList<>();

        for (int r = 0; r < db.length; r++) {
            boolean hasEmptyImportantCols = false;
            for (int icolNum : importantColNums) {
                if (db[r][icolNum].isEmpty()) {
                    hasEmptyImportantCols = true;
                    break;
                } else {
                    continue;
                }
            }
            if (!hasEmptyImportantCols) {
                keyRows.add(r);
            }
        }
        return keyRows;
    }
}
