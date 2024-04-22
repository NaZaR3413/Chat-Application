import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    // setup map to keep track of clients
    private static Map<String, PrintWriter> clientMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            // server is listening on port 8080
            server = new ServerSocket(8080);
            server.setReuseAddress(true);
            
            // infinite loop to search for client requests
            while(true) {
                // accept incoming client requests
                Socket client = server.accept();

                // display new client that connected to server
                System.out.println("New client connection: " + client.getInetAddress().getHostAddress());

                // create new thread object
                // implement clienthandler class:
                ClientHandler clientSocket = new ClientHandler(client);

                // thread to handle each client
                new Thread(clientSocket).start();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // constructor
        public ClientHandler(Socket _socket)
        {
            this.clientSocket = _socket;
        }

        public void run() {
            PrintWriter output = null;
            BufferedReader input = null;
            String clientName = null;

            try {
                    // get output of client 
                    output = new PrintWriter(clientSocket.getOutputStream(), true);

                    // get input of client 
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    clientName = input.readLine();
                    System.out.println("Client username: " + clientName);

                    // add user to clientMap
                    synchronized (clientMap) {
                        clientMap.put(clientName, output);
                    }

                    // string to read each client input
                    String line;
                    while((line = input.readLine()) != null) {
                        // print out recieved msg from client on server side
                        System.out.println("Sent from " + clientName + ": " + line);
                        //System.out.printf("Sent from client: %s\n", line);
                        //output.println(line);
                        //output.flush();
                        
                        // Assume the input format: "recipientName:message"
                        int separatorIndex = line.indexOf(":");

                        if (separatorIndex != -1) {
                            // extract intended recipient
                            String recipient = line.substring(0, separatorIndex);
                            // extract the message
                            String message = line.substring(separatorIndex + 1);
                            PrintWriter recipientOut = clientMap.get(recipient);
                            if (recipientOut != null) { 
                                recipientOut.println(clientName + ": " + message);
                                // flush out 
                                recipientOut.flush();
                            } else {
                                output.println("Recipient not found: " + recipient);
                                output.flush();
                            }
                        }
                    }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // remove client from map if added
                synchronized (clientMap) {
                    clientMap.remove(clientName);
                }
                try {
                    if(output != null) {
                        output.close();
                    }
                    if(input != null) {
                        input.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
