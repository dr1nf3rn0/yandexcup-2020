package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;

public class FTest {
    public static final String hostname = "localhost";
    public static final int port = 8080;

    @BeforeClass
    public static void createServer(){
        Undertow server = Undertow.builder()
                .addHttpListener(port,hostname)
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exchange.getResponseSender().send("[  \n" +
                                "  8,  \n" +
                                "  6,  \n" +
                                "  -2,  \n" +
                                "  2,  \n" +
                                "  4,  \n" +
                                "  17,  \n" +
                                "  256,  \n" +
                                "  1024,  \n" +
                                "  -17,  \n" +
                                "  -19  \n" +
                                "]");
                    }
                })
                .build();
        server.start();
    }

    @Test
    public void test1() throws IOException, ParseException {
//        Scanner scanner = new Scanner(System.in);
//        String host =  scanner.nextLine();
//        int port = Integer.parseInt(scanner.nextLine());
//        String a = scanner.nextLine();
//        String b = scanner.nextLine();
        String host = "http://localhost";
        int port = 8080;
        String a = "2";
        String b = "4";
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
        List<Long> result = jsonArray.stream().mapToLong(obj -> (long)obj).filter(x -> x < 0).sorted()
                .boxed().collect(Collectors.toList());
        List<Long> expected = Arrays.asList(-19L,-17L,-2L);
        assertEquals(expected,result);
    }
}
