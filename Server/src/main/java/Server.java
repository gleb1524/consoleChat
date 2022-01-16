import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket server;
    private static Socket socket;
    private static final int PORT = 8189;
    private static BufferedReader in;
    private static DataOutputStream out;
    private static BufferedReader consoleReader;
    public static void main(String[] args) {

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started!");
            socket = server.accept();
            System.out.println("Client connected!");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            Thread inputStream = new Thread(() -> {
                while (true){
                    String str = null;
                    try {
                        str = in.readLine();
                        System.out.println("Client: "+str);
                        if(str.equals("/end")){
                            out.writeUTF("Server down");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread outputStream = new Thread(() -> {
                while (true){
                    try {
                        out.writeUTF(consoleReader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            inputStream.start();
            outputStream.start();
            inputStream.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Client disconnected");
            System.exit(0);
            try {
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
