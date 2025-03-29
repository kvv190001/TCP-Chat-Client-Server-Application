import java.io.*;
import java.net.*;

class TCPServer {
  private static final int PORT = 9090;

  public static void main(String args[]) throws IOException {
    ServerSocket welcomeSocket = new ServerSocket(PORT);
    System.out.println("Server started. Waiting for connection!");

    // Continuously accept client connections in an infinite loop
    while (true) {
      Socket connectionSocket = welcomeSocket.accept();
      System.out.println("A new client has connected!");

      // Create a new ServerThread instance to handle communication with the client
      ServerThread serverThread = new ServerThread(connectionSocket);

      // Start the thread to handle the client in parallel to allow multiple clients
      Thread thread = new Thread(serverThread);
      thread.start();
    }
  }
}
