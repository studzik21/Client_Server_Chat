package chat;

import java.io.PrintWriter;
import java.net.InetAddress;

public class ClientIdentifier {
    PrintWriter out;
    String name;
    InetAddress address;
    int port;



    ClientIdentifier(PrintWriter out, String name,InetAddress address, int port){
        this.out = out;
        this.name= name;
        this.address=address;
        this.port=port;
    }
}
