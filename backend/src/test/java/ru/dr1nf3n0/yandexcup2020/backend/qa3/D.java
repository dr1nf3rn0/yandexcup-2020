package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Scanner;

public class D {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int feedNum = Integer.parseInt(scanner.nextLine());
        String[] feeds = new String[feedNum];
        for (int i = 0; i < feedNum; i++) {
            feeds[i] = scanner.nextLine();
        }
        JSONObject jsonResult =  processFeed(feedNum,feeds);
        System.out.println(jsonResult.toString());
    }

    public static JSONObject processFeed(int feedNum, String[] feeds) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResult = new JSONObject();
        JSONArray agg = new JSONArray();
        for (String feed : feeds) {
            JSONObject jsonObject = (JSONObject) (jsonParser.parse(feed));
            JSONArray offers = (JSONArray)jsonObject.get("offers");
            for (Object offer : offers
            ) {
                JSONObject offerObj = (JSONObject) offer;
                agg.add(offerObj);
            }
        }
        jsonResult.put("offers",agg);
        return jsonResult;
    }
}
