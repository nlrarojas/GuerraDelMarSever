package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import model.UserManager;
import util.IConstants;

/**
 *
 * @author Nelson
 */
public class Server extends Thread implements IConstants {

    public int port;
    public String conversation;
    public String instruction;
    private UserManager userManager;
    private ObjectOutputStream send;
    private ObjectInputStream receive;
    
    private boolean serverRunning;

    public Server(int pPort) {
        super("Servidor");
        this.port = pPort;
        this.instruction = "";
        this.conversation = "";
        this.userManager = new UserManager();
        this.serverRunning = true;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Iniciado");
            while (serverRunning) {
                try (Socket socket = serverSocket.accept()) {
                    send = new ObjectOutputStream(socket.getOutputStream());
                    receive = new ObjectInputStream(socket.getInputStream());
                    send.writeUnshared("Este es el servidor");
                    send.writeUnshared("listo");

                    instruction = receive.readObject().toString();
                    System.out.println(instruction);
                    executeInstruction(instruction);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void executeInstruction(String pInstruction) throws IOException, ClassNotFoundException {
        switch (instruction) {
            case VALIDATE_USER:
                String userName = receive.readUnshared().toString();
                if (userManager.validateUserExists(userName)) {
                    send.writeUnshared(RECHAZADO);
                } else {
                    userManager.addUser(userName);
                    send.writeUnshared(ACEPTED);
                }
                break;
            case GET_USERS:
                String list = "";
                for (int i = 0; i < userManager.getUserList().size(); i++) {
                    list += userManager.getUserList().get(i) + "#";
                }
                send.writeUnshared(list);
                send.writeUnshared(ACEPTED);
                break;
            case SEND_MESSAGE:
                conversation += receive.readUnshared().toString() + "#";
                send.writeUnshared(conversation);
                break;
            case UPDATE_CONVERSATION:
                send.writeUnshared(conversation);
            case USER_OFF:
                userName = receive.readUnshared().toString();
                System.out.println(userName);
                if (userManager.validateUserExists(userName)) {
                    userManager.getUserList().remove(userName);
                    send.writeUnshared(ACEPTED);
                } else {
                    send.writeUnshared(RECHAZADO);
                }
            case CHANGE_READY:                
                Player player = (Player)receive.readUnshared();
                userManager.addPlayer(player);
                send.writeUnshared(ACEPTED);
            default:
                break;
        }
    }

    public boolean isServerRunning() {
        return serverRunning;
    }

    public void setServerRunning(boolean serverRunning) {
        this.serverRunning = serverRunning;
    }
}
