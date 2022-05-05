package ja;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket(InetAddress.getByName("127.0.0.1"), 22333);

        /*BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        wr.write("hallo");
        wr.flush();*/


    }
}
