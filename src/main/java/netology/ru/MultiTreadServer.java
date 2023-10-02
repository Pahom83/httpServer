package netology.ru;

import java.io.IOException;
import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiTreadServer {
    private final ExecutorService service;

    public MultiTreadServer(int threads) {
        service = Executors.newFixedThreadPool(threads);
    }

    public void start(int port){
        while (true){
            try (ServerSocket mainServer = new ServerSocket(port)){
                service.submit(new Server(mainServer.accept()));
            } catch (IOException e) {
                e.printStackTrace();
                service.shutdown();
                break;
            }
        }
    }
}
