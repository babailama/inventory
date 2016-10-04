package ua.inventory.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ivanov-av
 * 04.10.2016 13:30.
 */
public class ServerWorker implements Runnable {

    protected Socket clientSocket = null;
    private BufferedReader input;
    private PrintWriter output;

    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            input = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ( )));
            output = new PrintWriter (new OutputStreamWriter (clientSocket.getOutputStream ( )));
            InetAddress clientInetAddress = clientSocket.getInetAddress ();
            long time = System.currentTimeMillis ( );
            output.println ("multithreaded server runs\n");
            output.println ("client " +  clientInetAddress.getCanonicalHostName () + " " + clientInetAddress.getHostAddress ());
            output.flush ( );

            while (true) {
                String str = input.readLine ( );
                if (str == null) {
                    break;
                }
                if (str.equalsIgnoreCase ("END")) {
                    break;
                }
                System.out.println (str); /* TODO logging */

            }
            output.close ( );
            input.close ( );
            System.out.println ("Request processed: " + time);
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }
}
