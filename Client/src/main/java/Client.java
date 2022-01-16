import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static final int PORT = 8189;
    private static final String HOST = "localhost";
    private static DataInputStream in;
    private static BufferedReader consoleReader;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            in = new DataInputStream(socket.getInputStream());
            Thread inputStream = new Thread(() -> {
                while (true){
                    try {
                        String str = in.readUTF();
                        System.out.println("Server: "+str);
                        if(str.equals("Server down")){
                            break;
                        }
                        if(str.equals("/end")){
                            out.println(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread outputStream = new Thread(() -> {
                while (true){
                    try {
                        out.println(consoleReader.readLine());
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
            System.out.println("Disconnected");
            System.exit(0);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
