import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {

        // establish connection to port
        try (Socket socket = new Socket("localhost", 8080)) {
            // writing to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // reading from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // set up scanner/reader objects
            Scanner scan = new Scanner(System.in);
            String line = null;

            // ask for username before we begin
            System.out.println("Please enter a username: ");
            line = scan.nextLine();
            output.println(line);
            output.flush();

            // establish exit as the keyword to end the process
            while(!"exit".equalsIgnoreCase(line)) {
                // scan in inputs
                line = scan.nextLine();

                // send inputs to server side
                output.println(line);
                output.flush();


                // verfy server reply
                String serverReply = null;
                // print server replies
                if((serverReply = input.readLine()) != null)
                {
                    //System.out.println("Server replies: " + input.readLine());
                    System.out.println("Server replies: " + serverReply);
                }

            }

            // close scan object when exit
            scan.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}