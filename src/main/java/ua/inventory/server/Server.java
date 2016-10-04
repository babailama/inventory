package ua.inventory.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by ivanov-av
 * 04.10.2016 10:34.
 */
public class Server implements Runnable {
    protected int serverPort; // tcp port to listen on
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public Server(int port) {
        this.serverPort = port;
    }

    public Server() {
        this.serverPort = ServerDefaultSettings.SERVER_PORT;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread ( );
        }
        openServerSocket ( );
        while (!isStopped ( )) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept ( );
            } catch (IOException e) {
                if (isStopped ( )) {
                    System.out.println ("Server Stopped.");
                    return;
                }
                throw new RuntimeException (
                        "Error accepting client connection", e);
            }
            new Thread (new ServerWorker (clientSocket)).start ( );
        }
        System.out.println ("Server Stopped.");
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket (this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException (String.format ("Cannot open port %d", this.serverPort), e);
        }
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close ( );
        } catch (IOException e) {
            throw new RuntimeException ("Error closing server", e);
        }
    }
}
