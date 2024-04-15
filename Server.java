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

            try {
                    // get output of client 
                    output = new PrintWriter(clientSocket.getOutputStream(), true);

                    // get input of client 
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String line;
                    while((line = input.readLine()) != null) {
                        // print out recieved msg from client
                        System.out.printf("Sent from client: %s\n", line);
                        output.println(line);
                    }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
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
