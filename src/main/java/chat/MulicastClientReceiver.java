package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Arrays;

public class MulicastClientReceiver extends Thread{
    MulticastSocket socket;
    String nick;
    Client client;

    public MulicastClientReceiver(Client client,MulticastSocket socket, String nick) {
        this.socket = socket;
        this.nick=nick;
        this.client=client;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];

        while (true) {
            try {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData(),0,receivePacket.getLength());
                if(!msg.startsWith(nick)) {
                    System.out.println(msg);
                    client.displayMessage(msg);
                }

            }
            catch (IOException e){
                break;
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
