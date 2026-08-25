import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebsocketServer {

  public static void main(String[] args) throws IOException {

    int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080; // Default port

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("WebSocket server started on port " + port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        Thread thread = new Thread(new ClientHandler(clientSocket));
        // Handle the client connection in a separate thread or method
        thread.start();
      }
    } catch (IOException e) {
      System.err.println("Error starting server: " + e.getMessage());
    }
  }
}