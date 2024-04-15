import java.io.*;
import java.net.*;

public class Server {
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
                // implement clienthandler class
                //ClientHandler clientSocket = new ClientHandler(client);

                // thread to handle each client
                //new Thread(clientSocket).start();

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
}
