package chat;



import sun.misc.Signal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class JavaTcpServer {
    List<ClientIdentifier> clients = new ArrayList<>();
    List<Thread> threads = new ArrayList<>();
    int portNumber = 12345;
    ServerSocket serverSocket = null;
    DatagramSocket datagramSocket = null;



    public void main() throws IOException {

        Signal.handle(new Signal("INT"), sig ->{
            System.out.println("Server exit. Closing clients connections.");
            endServer();
            System.out.println("Server ended");
            System.exit(0);
        });

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);
            datagramSocket = new DatagramSocket(portNumber);
            Thread thread =new UdpServerReceiver(datagramSocket,this);
            thread.start();
            threads.add(thread);
            while(true){
                // accept client
                Socket clientSocket = serverSocket.accept();
                thread= new ServerTcpClientReceiver(clientSocket,this);
                threads.add(thread);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
                serverSocket=null;
            }
        }
    }
    public void addClient(ClientIdentifier clientIdentifier) throws IOException {
        clients.add(clientIdentifier);
        System.out.println("Client: " + clientIdentifier.name + " added.");
    }
    public void broadcast(String message,String name) throws IOException {
        for (ClientIdentifier client: clients) {
            if(!client.name.equals(name)) {
                client.out.println(name +": " + message);
            }
        }
    }
    public void udpBroadcast(String message,int port) throws IOException {
        String name = getName(port);
        for (ClientIdentifier client: clients) {
            if(client.port!=port) {

                byte[] sendBuffer = (name+":\n"+message).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.address, client.port);
                datagramSocket.send(sendPacket);
            }
        }
    }
    public void removeClient(ClientIdentifier clientIdentifier) {
        clients.remove(clientIdentifier);
    }
    String getName(int port){
        for (ClientIdentifier client: clients)
            if(client.port==port) return client.name;
        return null;
    }
    void endServer(){
        for (Thread thread: threads) {
            try {
                if(thread.isAlive()){
                   thread.interrupt();
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }System.exit(0);
    }


}
