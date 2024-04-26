import java.io.*;
import java.util.*;
import java.net.*;
 
// Server class
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
                 
                System.out.println(received);
                 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.socket.close();
                    break;
                }

                if(received.equals("print userlist")) {
                    dos.writeUTF("User list: ");
                    for (ClientHandler mc : Server.ar) 
                    {
                        dos.writeUTF(mc.name);
                    }

                }
                 
                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
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
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}