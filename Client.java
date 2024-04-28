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
                        System.out.println("post sendfile print");
                        sendFile(dos);
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

    private static void sendFile(DataOutputStream dos)
    {
        JFileChooser j = new JFileChooser("d:");
 
        // Open the save dialog
        j.showSaveDialog(null);
    }

    // method to send files to server side
    /*private static void sendFile(DataOutputStream dos)
    {
        // have user pick a file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Please choose a .txt file to send");

        int result = fileChooser.showOpenDialog(null);
        System.out.println("beginning method print test");

        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToSend = fileChooser.getSelectedFile();

            try{
                // Send file metadata to the server
                dos.writeUTF(fileToSend.getName()); // Send file name
                dos.writeLong(fileToSend.length()); // Send file size
                
                // Send file content to the server
                try (FileInputStream fis = new FileInputStream(fileToSend)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, bytesRead);
                    }
                    dos.flush();
                } catch(FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }

                System.out.println("File sent successfully.");

            } catch(IOException e) {
                e.printStackTrace();
            }
            
        }
    }*/
} 
