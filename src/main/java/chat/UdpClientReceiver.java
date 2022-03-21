package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UdpClientReceiver extends Thread {
    DatagramSocket datagramSocket;
    Client client;
    public UdpClientReceiver(DatagramSocket datagramSocket, Client client) {
        this.datagramSocket = datagramSocket;
        this.client=client;
    }

    @Override
    public void run() {

        byte[] receiveBuffer = new byte[1024];

        while (true) {
            try {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData(),0,receivePacket.getLength());
                client.displayMessage(msg);
                System.out.println(msg);
            }
            catch (IOException e){
                break;
            }

        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        datagramSocket.close();
        datagramSocket=null;
    }
}
