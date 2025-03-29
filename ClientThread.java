import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket server;
    private BufferedReader inFromServer;

    // Constructor initializes the socket and input stream
    public ClientThread(Socket s) throws IOException {
        server = s;
        inFromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    @Override
    public void run() {
        try {
            // Continuously listen for messages from the server
            while (true) {
                // Get server response
                String serverResponse = inFromServer.readLine();

                // Check for specific error codes in the server's response and handle them
                if (serverResponse.contains("ERR 0")) {
                    System.out.println("[SERVER] ERR 0 - Username taken");
                    System.out.print("Reenter username: ");
                } else if (serverResponse.contains("ERR 1")) {
                    System.out.println("[SERVER] ERR 1 - Username too long");
                    System.out.print("Reenter username: ");
                } else if (serverResponse.contains("ERR 2")) {
                    System.out.println("[SERVER] ERR 2 - Username contains spaces");
                    System.out.print("Reenter username: ");
                } else if (serverResponse.contains("ERR 3")) {
                    System.out.println("[SERVER] ERR 3 - Unknown user for private message");
                    System.out.print("Reenter message: ");
                } else if (serverResponse.contains("ERR 4")) {
                    System.out.println("[SERVER] ERR 4 - Unknown message format");
                    System.out.print("Reenter message: ");
                } else {
                    System.out.println("[SERVER] " + serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inFromServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
