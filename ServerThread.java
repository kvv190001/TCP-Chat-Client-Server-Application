import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ServerThread implements Runnable {
    public static ArrayList<ServerThread> clients = new ArrayList<>();
    private Socket client;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private String clientName;

    // Constructor initializes the socket and input/output streams for the client
    public ServerThread(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        this.inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.outToClient = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String clientSentence;
            String userName;

            // Loop to continually read messages from the client
            while (true) {
                // Read a message from the client
                clientSentence = inFromClient.readLine();
                System.out.println("FROM " + clientName + ": " + clientSentence);
                String[] parts = clientSentence.split(" ");

                // Handle registration of a new username
                if (Objects.equals(parts[0], "REG")) {
                    userName = parts[1];
                    verifyName(userName);
                } else if(Objects.equals(parts[0], "MESG") && parts.length == 2){
                    // Handle a broadcast message
                    outToAll(clientName + " to everyone: " + parts[1] + '\n');
                } else if(Objects.equals(parts[0], "PMSG") && parts.length == 3){
                    // Handle a private message
                    for(ServerThread aClient : clients) {
                        if(aClient.clientName.equals(parts[1])){
                            aClient.outToClient.writeBytes(clientName + " says: " + parts[2] + '\n');
                        }
                    }
                } else if(Objects.equals(parts[0], "EXIT")){
                    // Handle client disconnection
                    outToClient.writeBytes("ACK\n");
                    clients.remove(this);
                    outToAll(clientName + " has left the chat!\n");
                    break;
                } else{
                    // Send error message for invalid commands
                    outToClient.writeBytes("ERR 4\n");
                }
            }
        } catch (IOException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        } finally {
            // Clean up resources when the client disconnects
            try {
                outToClient.close();
            } catch (IOException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
            try {
                inFromClient.close();
            } catch (IOException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void verifyName(String username) throws IOException {
        // Check if the username is too long
        if (username.length() > 32) {
             outToClient.writeBytes("ERR 1 \n");
             return;
        } else if (username.contains(" ")) {
             // Check if the username contains spaces
             outToClient.writeBytes("ERR 2 \n");
             return;
        }

        String[] parts = username.split(" ");
        if(parts.length > 2){
            outToClient.writeBytes("ERR 2 \n");
            return;
        }

        // Check if the username is already in use
        for(ServerThread aClient : clients) {
            if(aClient.clientName.equals(username)){
                outToClient.writeBytes("ERR 0 \n");
                return;
            }
        }

        // Set the username and add the client to the list
        this.clientName = username;
        clients.add(this);
        outToClient.writeBytes("ACK " + clients.size() + " {");

        for(int i = 0; i < clients.size(); i++){
            ServerThread aClient = clients.get(i);
            outToClient.writeBytes(aClient.clientName);
            if(i < clients.size() - 1){
                outToClient.writeBytes(", ");
            }
        }
        outToClient.writeBytes("}\n");
        outToAll(clientName + " has entered the chat!\n");
    }

    // Send a broadcast message to all clients
    private void outToAll(String msg) throws IOException {
        for (ServerThread aClient : clients) {
            if(!aClient.clientName.equals(clientName)){
                aClient.outToClient.writeBytes(msg);
            }
        }
    }

    
}
