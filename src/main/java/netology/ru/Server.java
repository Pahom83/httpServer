package netology.ru;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Server extends Thread {
    final BufferedReader in;
    final BufferedOutputStream out;
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");


    public Server(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedOutputStream(socket.getOutputStream());
        start();
    }


    @Override
    public void run() {
        String requestLine;
        String[] parts;
        try {
//            while (true) {
                requestLine = in.readLine();
                parts = requestLine.split(" ");

                if (parts.length != 3) {
                    // just close socket
                    return;
                }
                final String path = parts[1];
                if (!validPaths.contains(path)) {
                    sendNotFound();
                    return;
                }
                final var filePath = Path.of(".", "public", path);
                final var mimeType = Files.probeContentType(filePath);

                // special case for classic
                if (path.equals("/classic.html")) {
                    final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    sendOk(content, mimeType);
                    return;
                }
                sendOk(Files.readAllBytes(filePath), mimeType);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOk(byte[] content, String mimeType) throws IOException {
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
//        Files.copy(filePath, out);
        out.flush();

    }

    private void sendNotFound() throws IOException {
        out.write((
                """
                        HTTP/1.1 404 Not Found\r
                        Content-Length: 0\r
                        Connection: close\r
                        \r
                        """
        ).getBytes());
        out.flush();
    }
}
