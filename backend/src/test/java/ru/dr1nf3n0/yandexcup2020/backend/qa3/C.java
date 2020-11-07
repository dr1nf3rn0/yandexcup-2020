package ru.dr1nf3n0.yandexcup2020.backend.qa3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class C {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(7777), 0);
        server.createContext("/ping", new HttpHandler() {

            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    httpExchange.sendResponseHeaders(200, 0);
                }
            }
        });
        server.createContext("/shutdown", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                server.stop(0);
            }
        });
        server.createContext("/validatePhoneNumber", new HttpHandler() {

            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    String requestPath = URLDecoder.decode(httpExchange.getRequestURI().toString(), "UTF8");
                    String getParams = extractContextParams("/validatePhoneNumber", requestPath);
                    if (getParams != null) {
                        String phoneNumber = extractGetRequestParam("phone_number", getParams);
                        if (phoneNumber != null) {
                            OutputStream out = httpExchange.getResponseBody();
                            String normalizedNumber = normalizePhoneNumber(phoneNumber);
                            if (normalizedNumber != null) {
                                JSONObject jsonObject = createValidationResponse(normalizedNumber, "true");
                                httpExchange.sendResponseHeaders(200, jsonObject.toString().length());
                                out.write(jsonObject.toString().getBytes());
                                out.flush();
                                out.close();
                            } else {
                                JSONObject jsonObject = createValidationResponse("", "false");
                                httpExchange.sendResponseHeaders(200, jsonObject.toString().length());
                                out.write(jsonObject.toString().getBytes());
                                out.flush();
                                out.close();
                            }
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }

                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }

            }
        });
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }

    public static String extractContextParams(String context, String getPath) {
        String[] path = getPath.split("\\?");
        if (path.length > 0 && path[0].equals(context)) {
            return path[1];
        }
        return null;
    }

    public static String extractGetRequestParam(String paramName, String paramsString) {
        String[] kvs = paramsString.split("&");
        for (String kv :
                kvs) {
            if (kv.startsWith(paramName)) {
                String[] kvSplit = kv.split("=");
                return kvSplit[1];
            }
        }
        return null;
    }

    public static Pattern pattern = Pattern.compile(
            "(\\+7|8)((982|986|912|934)\\d{7}|\\s(982|986|912|934)\\s(\\d{3}\\s\\d{4}|\\d{3}\\s\\d{2}\\s\\d{2}|\\d{7})|\\s\\((982|986|912|934)\\)\\s\\d{3}-\\d{4})"
    );

    public static String normalizePhoneNumber(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String normalizeInput = input.replaceAll("\\D", "").substring(1);
            String result = "+7-" + normalizeInput.substring(0, 3) + "-" +
                    normalizeInput.substring(3, 6) + "-" +
                    normalizeInput.substring(6);
            return result;
        }
        return null;
    }


    public static JSONObject createValidationResponse(String normalized, String status) {
        JSONObject jsonObject = new JSONObject();
        if (status.equalsIgnoreCase("true")) {
            jsonObject.put("normalized", normalized);
            jsonObject.put("status", Boolean.valueOf(status));
            return jsonObject;
        } else if (status.equalsIgnoreCase("false")) {
            jsonObject.put("status", Boolean.valueOf(status));
            return jsonObject;
        } else {
            //something wrong happened
            return jsonObject;
        }
    }
}
