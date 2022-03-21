package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class UdpServerReceiver extends Thread{
    DatagramSocket socket;
    JavaTcpServer server;
    UdpServerReceiver(DatagramSocket socket, JavaTcpServer server){
        this.socket= socket;
        this.server=server;
    }

    @Override
    public void run() {

        byte[] receiveBuffer = new byte[1024];

        while(true) {
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData(),0,receivePacket.getLength());
                server.udpBroadcast(msg,receivePacket.getPort());
                System.out.println("U" + server.getName(receivePacket.getPort())+ ":" +  msg);
            }catch (SocketException e){
                System.out.println("UDP socket closed");
                break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        socket.close();
        socket=null;
    }
}

