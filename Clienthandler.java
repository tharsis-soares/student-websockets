import java.io.Exception;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;


public class Clienthandler implements Runnable {
  private final Socket socket;

  public Clienthandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    // Handle the client connection
    try (Socket s = socket) {
      InputStream input = s.getInputStream();
      OutputStream output = s.getOutputStream();
      // Implement WebSocket handshake and communication here
    } catch (Exception e) {
      System.err.println("Error handling client: " + e.getMessage());
    }

  }
}
