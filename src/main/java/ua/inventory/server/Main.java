package ua.inventory.server;

/**
 * Created by ivanov-av
 * 04.10.2016 13:34.
 */
public class Main {
    public static void main(String args[]){
        Server server = new Server ();
        new Thread (server).start ();

    }
}
