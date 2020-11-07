package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class CTest {


    @BeforeClass
    public static void init() throws IOException {
        C.main(new String[]{});
    }

    @Test
    public void badPathTest() throws IOException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/fakePath?phone_number=89820000000");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        assertEquals(404, connection.getResponseCode());
    }

    @Test
    public void badQueryTest() throws IOException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/validatePhoneNumber?fake_parameter=89820000000");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        assertEquals(400, connection.getResponseCode());
    }

    @Test
    public void phoneValidationFailedTest() throws IOException, ParseException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/validatePhoneNumber?phone_number=80000000000");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        assertEquals(200, connection.getResponseCode());
        JSONObject expected = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(sb.toString());
        expected.put("status", false);
        assertEquals(expected, result);
    }

    @Test
    public void phoneValidationSuccessTest() throws IOException, ParseException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/validatePhoneNumber?phone_number=89820000000");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        assertEquals(200, connection.getResponseCode());
        JSONObject expected = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(sb.toString());
        expected.put("status", true);
        expected.put("normalized", "+7-982-000-0000");
        assertEquals(expected, result);
    }

    @Test
    public void phoneValidationURLEncodedTest() throws IOException, ParseException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/validatePhoneNumber?phone_number=%2B7+%28982%29+123-1234");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        assertEquals(200, connection.getResponseCode());
        JSONObject expected = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(sb.toString());
        expected.put("status", true);
        expected.put("normalized", "+7-982-123-1234");
        assertEquals(expected, result);
    }


    @Test
    public void pingTest() throws IOException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/ping");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        assertEquals(200, connection.getResponseCode());
    }

    @AfterClass
    public static void shutdownTest() throws IOException {
        String host = "http://localhost";
        int port = 7777;
        URL url = new URL(host + ":" + port + "/shutdown");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        try {
            HttpURLConnection retry = (HttpURLConnection) url.openConnection();
            retry.setRequestMethod("GET");
            retry.setConnectTimeout(10000);
            retry.connect();
        }
        catch (Exception e){
            assertEquals(IOException.class, e.getClass());
         }
    }

    @Test
    public void testRegExp1(){
        String num = "+7 (982) 123-1234";
        Pattern pattern  = C.pattern;
        Matcher matcher = pattern.matcher(num);
        assertEquals(true, matcher.matches());
    }

    @Test
    public void testRegExp2(){
        String num = "89820000000";
        Pattern pattern  = C.pattern;
        Matcher matcher = pattern.matcher(num);
        assertEquals(true, matcher.matches());
    }

    @Test
    public void testRegExp3(){
        String num = "+7 982 123 12 34";
        Pattern pattern  = C.pattern;
        Matcher matcher = pattern.matcher(num);
        assertEquals(true, matcher.matches());
    }
}


