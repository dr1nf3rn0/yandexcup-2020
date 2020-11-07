package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.dr1nf3n0.yandexcup2020.backend.qa3.D.processFeed;

public class DTest {

    @Test
    public void test1() throws ParseException {
        int feedNum = 2;
        String[] feeds = new String[]{
                "{\"offers\": [{\"offer_id\": \"offer1\", \"market_sku\": 10846332, \"price\": 1490}, {\"offer_id\": \"offer2\", \"market_sku\": 682644, \"price\": 499}]}",
                "{\"offers\": [{\"offer_id\": \"offer3\", \"market_sku\": 832784, \"price\": 14000}]}"
        };
        JSONObject result = processFeed(feedNum,feeds);
        JSONParser parser = new JSONParser();
        JSONObject expected = (JSONObject) parser.parse("{\"offers\":[{\"price\":1490,\"market_sku\":10846332,\"offer_id\":\"offer1\"},{\"price\":499,\"market_sku\":682644,\"offer_id\":\"offer2\"},{\"price\":14000,\"market_sku\":832784,\"offer_id\":\"offer3\"}]}\n");
        assertEquals(expected,result);
    }
}
