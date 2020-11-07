package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class F {

    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        String host =  scanner.nextLine();
        int port = Integer.parseInt(scanner.nextLine());
        String a = scanner.nextLine();
        String b = scanner.nextLine();
        URL url = new URL(host+":"+port+"?a="+a+"&b="+b);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while( (line = in.readLine()) != null){
            sb.append(line);
        }
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(sb.toString());
        jsonArray.stream().mapToLong(obj -> (long)obj).filter(x -> x < 0).sorted().forEach(System.out::println);
    }
}
