package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReceiver extends Thread{
    BufferedReader in;
    PrintWriter out;
    Client client;
    Socket socket;
    ClientReceiver(Socket socket, BufferedReader in, PrintWriter out, Client client){
        this.in =in;
        this.client=client;
        this.socket = socket;
        this.out=out;
    }
    boolean state=false;
    @Override
    public void run() {
        while (true){
            try {
                if(in!= null) {
                    String mes = in.readLine();
                    if (mes == null) {
                        state=true;
                        break;
                    }
                    client.displayMessage(mes);
                    System.out.println(mes);
                }
            } catch (IOException e) {
                break;
            }

        }

        if(socket!=null) {
            try {
                socket.close();
                socket = null;

            } catch (IOException e) {
            e.printStackTrace();
            }
        }
        if(state) client.endClient(true);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket=null;

    }
}
