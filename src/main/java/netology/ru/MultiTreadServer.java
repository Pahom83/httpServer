package netology.ru;

import java.io.IOException;
import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiTreadServer {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(64);
//        final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
        final int port = 9999;
        while (true){
            try (ServerSocket mainServer = new ServerSocket(port)){
//                Socket socket = mainServer.accept();
                service.submit(new Server(mainServer.accept()));
            } catch (IOException e) {
                e.printStackTrace();
                service.shutdown();
                break;
            }
        }
    }
}
