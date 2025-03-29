# TCP-Chat-Client-Server-Application
------------------------------------

Description:  This TCP-based chat application allows multiple clients to communicate with each other through a chat server. The server manages user registrations, broadcasts, private messaging, and user exits. When a client connects, it sends a registration message with a username, and the server verifies the username before allowing the user to join the chat room.

Platform/compiler: UNIX, javac

Files:

TCPClient.java - Source code for client application.
ClientThread.java - Source code for client thread.
TCPServer.java - Source code for server application.
ServerThread.java - Source code for server thread.

Compiling instructions:

To compile, place all files within a single directory and type "javac *.java".

Running instructions:
- Type "java TCPServer" on the server host.  
- Type "java TCPClient netxx" on the client host, where netxx is the name of
  the server host.
- To exit the client or server, type CTRL-C.

Message Format:
+ Client to Server:
    - REG <username>: Register a new user.
    - MESG <message>: Send a broadcast message.
    - PMSG <user> <message>: Send a private message to a specific user.
    - EXIT: Inform the server that the user is leaving.

+ Server to Client:
    - ACK <#users> <list_of_users>: Acknowledge successful registration and provide user list.
    - MSG <username> <message>: Forward chat messages.
    - ERR <errorcode>: Error message for registration issues or message delivery.

Server-side Error Codes:
0: Username already taken.
1: Username too long.
2: Username contains spaces.
3: Unknown user for private message.
4: Unknown message format.
