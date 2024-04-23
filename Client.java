import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {

        Scanner scan = new Scanner(System.in);
        // establish connection to port
        try (Socket socket = new Socket("localhost", 8080)) {
            // writing to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // reading from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            DataInputStream dis = new DataInputStream(socket.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 

            // set up scanner/reader objects
            String line = null;

            // ask for username before we begin
            System.out.println("Please enter a username: ");
            line = scan.nextLine();
            output.println(line);
            output.flush();

            // write message thread
            Thread sendMessage = new Thread(new Runnable()  
            { 
                @Override
                public void run() { 
                    while (true) { 
    
                        // read the message to deliver. 
                        String msg = scan.nextLine(); 
                        
                        try { 
                            // write on the output stream 
                            dos.writeUTF(msg); 
                        } 
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    } 
                } 
            }); 

            // readMessage thread 
            Thread readMessage = new Thread(new Runnable()  
            { 
                @Override
                public void run() { 
    
                    while (true) { 
                        try { 
                            // read the message sent to this client 
                            String msg = dis.readUTF(); 
                            System.out.println(msg);
                        } catch (IOException e) { 
    
                            e.printStackTrace(); 
                        } 
                    } 
                } 
            }); 

            // start both threads
            sendMessage.start();
            readMessage.start();

            // establish exit as the keyword to end the process
            /*while(!"exit".equalsIgnoreCase(line)) {
                // scan in inputs
                line = scan.nextLine();

                // send inputs to server side
                output.println(line);
                output.flush();


                // verfy server reply and print server reply
                String serverReply = null;
                if((serverReply = input.readLine()) != null)
                {
                    //System.out.println("Server replies: " + input.readLine());
                    System.out.println("Server replies: " + serverReply);
                }

            }*/

            // close scan object when exit
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            scan.close();
        }
    }
}