package chat;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    Thread udpThread;
    Thread tcpThread;
    Thread mulicastThread;
    Socket socket = null;
    DatagramSocket datagramSocket=null;
    MulticastSocket multicastSocket;
    String nick;
    PrintWriter out;
    BufferedReader in;
    BufferedReader userInput;
    TextArea textArea;
    CommunicationType type=CommunicationType.TCP;
    InetAddress address;
    InetAddress multiAddress;
    InetSocketAddress multicaastAddress;
    int portNumber= 12345;
    String hostName = "localhost";

    public Client(TextArea textArea, String nick) {
        this.textArea=textArea;
        this.nick=nick;
    }


    public void main() {

        Signal.handle(new Signal("INT"), sig ->{
            System.out.println("Client exit.");
            endClient(false);
            System.out.println("Client ended");
            System.exit(0);
        });

        System.out.println("JAVA TCP CLIENT");

        try {

            createSockets();
            createStreams();
            createThreads();
            out.println(nick);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void endClient(boolean state) {
        if(mulicastThread!= null){
            if(mulicastThread.isAlive()){
                mulicastThread.interrupt();
                try {
                    mulicastThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mulicastThread=null;
            }
        }


        if(udpThread!=null)
            if(udpThread.isAlive()) {
                udpThread.interrupt();
                    try {
                        udpThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    udpThread=null;
            }
        if(tcpThread!=null)
            if(tcpThread.isAlive()){
                if(!state){ tcpThread.interrupt();
                try {
                    tcpThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
                tcpThread=null;
            }
        System.out.println("Client ended");
        System.exit(0);
    }
    public void send(String message){
        displayMessage("Me: " + message);
        if(type==CommunicationType.TCP){
            out.println(message);
        }

        else if(type==CommunicationType.UDP){
            byte[] sendBuffer = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
            try {
                datagramSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type==CommunicationType.MULTICAST){
            byte[] sendBuffer = (nick+": " +message).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, multiAddress, 4444);
            try {
                multicastSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(String message){
        textArea.appendText(message+"\n");

    }
    public void changeMode(CommunicationType type){
        this.type=type;
    }

    private void createSockets() throws IOException {
        address = InetAddress.getByName(hostName);
        socket = new Socket(hostName, portNumber);
        datagramSocket = new DatagramSocket(socket.getLocalPort());
        multicastSocket = new MulticastSocket(4444);
        multiAddress = InetAddress.getByName("230.0.0.1");
        multicaastAddress = new InetSocketAddress(multiAddress,socket.getLocalPort());
        multicastSocket.joinGroup(multicaastAddress,null);
    }

    void createStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userInput = new BufferedReader(new InputStreamReader(System.in));
    }
    private void createThreads() {
        tcpThread= new ClientReceiver(socket,in,out,this);
        tcpThread.start();

        udpThread = new UdpClientReceiver(datagramSocket,this);
        udpThread.start();

        mulicastThread = new MulicastClientReceiver(this,multicastSocket,nick);
        mulicastThread.start();
    }

}
