package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;
import model.UserManager;
import util.IConstants;

/**
 *
 * @author Nelson
 */
public class Server extends Thread implements IConstants{
    public int port;
    public String conversation;
    public String instruction;
    private UserManager userManager;
    
    public Server(int pPort){
        super("Servidor");
        this.port = pPort;
        this.instruction = "";
        this.conversation = "";
        userManager = new UserManager();
    }
    
    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Iniciado");
            do {
                try (Socket socket = serverSocket.accept()) {
                    PrintStream send = new PrintStream(socket.getOutputStream());
                    BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    send.println("Este es el servidor");
                    send.println("listo");
                    
                    instruction = receive.readLine();
                    switch (instruction) {
                        case VALIDATE_USER:
                            String userName = receive.readLine();
                            if(userManager.validateUserExists(userName)){
                                send.println(RECHAZADO);
                            }else{
                                userManager.addUser(userName);
                                send.println(ACEPTED);
                            }   break;
                        case GET_USERS:
                            String list = "";
                            for (int i = 0; i < userManager.getUserList().size(); i++) {
                                list += userManager.getUserList().get(i) + "#";
                            }   send.println(list);
                            send.println(ACEPTED);
                            break;
                        case SEND_MESSAGE:
                            conversation += receive.readLine() + "#";
                            send.println(conversation);
                            break;
                        case UPDATE_CONVERSATION:
                            send.println(conversation);
                        case USER_OFF:
                            userName = receive.readLine();
                            if(userManager.validateUserExists(userName)){
                                userManager.getUserList().remove(userName);
                                send.println(ACEPTED);
                            }else{
                                send.println(RECHAZADO);
                            }
                        default:
                            break;
                    }
                }                        
            } while (true);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
