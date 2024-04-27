import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class Client  
{ 
    final static int ServerPort = 8080; 
  
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in); 
          
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket socket = new Socket(ip, ServerPort); 
          
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(socket.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 

        // read in username
        System.out.println("Enter username");
        String username = scn.nextLine();

        try {
            dos.writeUTF(username);
        } catch(IOException e) {
            e.printStackTrace();
        }
  
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver. 
                    String msg = scn.nextLine(); 
                      
                    try { 
                        // write on the output stream 
                        dos.writeUTF(msg); 
                    } catch (IOException e) { 
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
                    } catch (EOFException eof) { // client logsout, server shuts down this client thread
                        System.out.println("Connection closed by the server.");
                        // close all processes and exit
                        try {
                            dos.close();
                            dis.close();
                            socket.close();
                            System.exit(0);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 
            }
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
  
    } 
} 
