package chat;

import java.io.IOException;

public class ServerStart {
    public static void main(String[] args) throws IOException {
        JavaTcpServer javaTcpServer = new JavaTcpServer();
        javaTcpServer.main();
    }
}
