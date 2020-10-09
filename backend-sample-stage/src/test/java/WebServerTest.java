import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class WebServerTest {

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

    private String createHttpRequest(String path,String hostname,int port) {
        return String.join(System.getProperty("line.separator"),
                "GET " + path + " HTTP/1.1",
                "Host: "+hostname+":"+port,
                "User-Agent:Grabber",
                "Accept: application/json",
                "Connection: keep-alive",
                ""
                );
    }

    @Test
    @Ignore("Doesn't work")
    public void getResponseTestUsingNIO() throws IOException {
        SocketChannel channel = SocketChannel.open();
        SocketAddress address = new InetSocketAddress(hostname,port);
        channel.connect(address);
        String httpRequest = createHttpRequest("/",hostname,port);
        ByteBuffer requestBuffer = ByteBuffer.wrap(httpRequest.getBytes(StandardCharsets.UTF_8));
        channel.write(requestBuffer);


        ByteBuffer buffer= ByteBuffer.allocateDirect(1024);
        int bytesRead = channel.read(buffer);
        System.out.println(bytesRead);
        //        while(bytesRead != -1){
//            buffer.flip();
//            while (buffer.hasRemaining()){
//                System.out.println((char)buffer.get());
//            }
//
//            buffer.clear();
//            bytesRead = channel.read(buffer);
//        }

        channel.close();
    }

    @Test
    @Ignore("Doesn't work")
    public void getResponseTestUsingSockets() throws IOException {
        Socket socket = new Socket("0.0.0.0",port);
        socket.setSoTimeout(10000);
//        socket.connect(new InetSocketAddress(hostname,port));
        OutputStream out = socket.getOutputStream();
        String httpRequest = createHttpRequest("/",hostname,port);
        out.write(httpRequest.getBytes(StandardCharsets.UTF_8));
        out.flush();
        byte [] buffer = new byte[1024];
        socket.getInputStream().read(buffer);
        System.out.println(buffer);

        out.close();
        socket.close();
    }

    @Test
    public void getResponseTestUsingHTTPConnection() throws IOException, ParseException {
        URL url = new URL("http",hostname,port,"/");
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
        JSONArray  jsonArray = (JSONArray) parser.parse(sb.toString());
        int sum = 0;
        for (Object item: jsonArray) {
            sum+=(long)item;
        }
        in.close();
        connection.disconnect();
        assert sum == 1279;
    }
}
