import java.io.*; 
import java.net.*; 
  
class TCPClient { 
  public static void main(String args[]) throws Exception {
    int servPort = 9090;
    String hostName = null;

    try {
        hostName = args[0];
        if(!hostName.contains(".utdallas.edu"))
          hostName = hostName + ".utdallas.edu";
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Need argument: remoteHost");
        System.exit(-1);
    }

    //Create a TCP socket to server
    Socket clientSocket = new Socket(hostName,servPort);
    ClientThread serverConn = new ClientThread(clientSocket);
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    

    new Thread(serverConn).start();

    // Obtain username from user
    System.out.print("Enter username: ");

    //Get user input and send appropriate message to server
    String userInput;
    while ((userInput = inFromUser.readLine()) != null) {
        outToServer.writeBytes(userInput + '\n');
        
      //If user wants to exit, break out of loop
      if(userInput.contains("EXIT")){
        break;
      }
    }
    clientSocket.close();
  } 
} 

