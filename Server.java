import java.io.*;
import java.util.*;
import java.net.*;
 
public class Server 
{
 
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
 
    public static void main(String[] args) throws IOException 
    {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(8080);
        Socket socket;
         
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            socket = ss.accept();
 
            System.out.println("New client request received : " + socket);
             
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // do/while switch reader
            int options = 0;
            boolean validLogin = false;

            do {
                options = dis.readInt();
                switch(options) 
                {
                    case 1: // login
                    // read username
                    // read password
                    // confirm authentication
                    // if authentication is confirmed, 
                        // read in user's id
                        // create clienthandler object for user
                        // add user to active client's list
                        // create/start user's thread and set validLogin to true

                    // if false, reject and have user try again or create an account
                    break;

                    case 2: // create an account
                    // read in a username
                    // read in a password
                    // attempt to create the account
                    // if successful 
                        // read in user's id
                        // create clienthandler object for user
                        // add user to active client's list
                        // create/start user's thread and set validLogin to true
                        
                    // else, reject, have user try again
                    break;

                    case 3: // user wants to print the menu again 
                    // do nothing, will be handled on the client side
                    break;

                    default: // do nothing, will be handled on the client end
                    break;
                }

            } while (validLogin != true);
             
            System.out.println("Creating a new handler for this client...");
            
            // listen for and pull username from client side
            String username = dis.readUTF();
            
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(socket,username, dis, dos);
 
            // Create a new Thread with this object.
            Thread thread = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
 
            // add this client to active clients list
            ar.add(mtch);
 
            // start the thread.
            thread.start();
 
        }
    }
}
 
// ClientHandler class
class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket socket;
    boolean isloggedin;
     
    // constructor
    public ClientHandler(Socket socket, String name,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.socket = socket;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received;
        while (true) 
        {
            try
            {
                // receive the string
                received = dis.readUTF();
                 
                System.out.println(this.name + " messaged: " + received);
                 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    synchronized (Server.ar) {
                        Server.ar.remove(this);
                    }
                    this.socket.close();
                    break;
                }

                if(received.equals("print userlist")) {
                    this.dos.writeUTF("User list: ");
                    for (ClientHandler mc : Server.ar) 
                    {
                        dos.writeUTF(mc.name);
                    }
                    // return to the top of loop to avoid further processing
                    continue;

                }
                else if(received.equals("file incoming")) 
                {
                    // parse through incoming file and download file to directory
                    receiveFile(dis, this.name);
                    // return to the top of the loop to avoid further processing
                    continue;
                }

                // check for proper recipient formatting 
                if(received.contains("#"))
                {
                    // break the string into message and recipient part
                    // stringtokenizer seperates words before and after an '#'
                    StringTokenizer st = new StringTokenizer(received, "#");

                    // verify that at message and intended recipient are present
                    if(st.countTokens() >= 2) {
                        String MsgToSend = st.nextToken();
                        String recipient = st.nextToken();

                        // search for the recipient in the connected devices list.
                        // ar is the vector storing client of active users
                        for (ClientHandler mc : Server.ar) 
                        {
                            // if the recipient is found, write on its
                            // output stream
                            if (mc.name.equals(recipient) && mc.isloggedin==true) 
                            {
                                mc.dos.writeUTF(this.name+" : "+ MsgToSend);
                                break;
                            }
                        }
                    }
                    else 
                    {
                        dos.writeUTF("ERROR: Invalid message format. 2 or more tokens not present");
                    }
                }
                else{
                    dos.writeUTF("ERROR: Invalid message. Expected 'message#recipient'");
                }
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
            this.socket.close();
            scn.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // method to accept files send by clients
    private static void receiveFile(DataInputStream dis, String name) throws IOException
    {
         // recieve metadata
         String fileName = dis.readUTF();
         long fileSize = dis.readLong();

         // save file
         File receivedFile = new File(fileName);
         try (FileOutputStream fos = new FileOutputStream(receivedFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            long remaining = fileSize;
            while (remaining > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                fos.write(buffer, 0, bytesRead);
                remaining -= bytesRead;
            }
            System.out.println("Saved: " + fileName + " from: " + name + " to current directory");
            fos.close();
        } 

    }
}