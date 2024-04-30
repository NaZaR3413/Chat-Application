import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import javax.swing.*;
  
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

                    if(msg .equals("send file")) 
                    {
                        //System.out.println("post sendfile print");
                        sendFile(dos, scn);
                    }
                    else 
                    {
                        try { 
                            // write on the output stream 
                            dos.writeUTF(msg); 
                        } catch (IOException e) { 
                            e.printStackTrace(); 
                        } 
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

    // method to send files to server side
    private static void sendFile(DataOutputStream dos, Scanner scn)
    {
        // grab file path from client 
        System.out.println("Please enter the exact path of the file you would like to send: ");
        String filePath = scn.nextLine();

        File fileToSend = new File(filePath);

        // verify file exists and proceed if it is C:\Users\nilay\Downloads\Hackathon txt.txt
        if(fileToSend.exists() && fileToSend.isFile())
        {
            //System.out.println("Yes exists");

            // Send file metadata to the server
            try {
                // signal for the server to know when a file is coming
                dos.writeUTF("file incoming");
                dos.writeUTF(fileToSend.getName()); // Send file name
                dos.writeLong(fileToSend.length()); // Send file size

                FileInputStream fis = new FileInputStream(fileToSend);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                dos.flush();

                System.out.println("file send successfully!");
                fis.close();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("ERROR: File does not exist or is not a valid file");
        }

    }
} 
