package main;

import controller.Server;
import util.IConstants;

/**
 *
 * @author Nelson
 */
public class MainProgram implements IConstants{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server myServer = new Server(SERVER_PORT);
        myServer.start();
    }    
}
