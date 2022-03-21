package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTcpClientReceiver extends Thread {
    String name;
    Socket clientSocket;
    JavaTcpServer server;
    chat.State state = chat.State.INIT;
    ClientIdentifier clientIdentifier;


    ServerTcpClientReceiver(Socket socket, JavaTcpServer server) {
        this.clientSocket = socket;
        this.server= server;
    }


    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            String msg = null;
            try {
                assert in != null;
                msg = in.readLine();
                if(state== chat.State.INIT){
                    name = msg;
                    clientIdentifier= new ClientIdentifier(out,msg,clientSocket.getInetAddress(),clientSocket.getPort());
                    server.addClient(clientIdentifier);
                    state= chat.State.CHAT;
                }
                else{
                    if(msg==null){
                        break;
                    }
                    server.broadcast(msg,name);
                    System.out.println("T"+name+": "+ msg);
                }
            } catch (IOException e) {
                break;
            }

        }
        System.out.println(name + ": disconnected");
        server.removeClient(clientIdentifier);
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Unable to close client's socket");
        }
    }

    @Override
    public void interrupt() {
        try {
            server.removeClient(clientIdentifier);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
