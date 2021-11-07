import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMaster extends Thread {

  private List<ServerDTO> servidores;
  private int puertoDisponible;

  private ServerSocket serverOutput;
  private ServerSocket serverInput;
  private ObjectOutputStream salida;
  private DataInputStream entrada;

  public ServerMaster() {
    this.servidores = new ArrayList<ServerDTO>();
    this.puertoDisponible = 50002;

    try {
      this.serverOutput = new ServerSocket(50000);
      this.serverInput = new ServerSocket(50001);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {

    while (true) {
      try {
        Socket socket = serverOutput.accept();
        salida = new ObjectOutputStream(socket.getOutputStream());
        salida.writeObject(servidores);
        salida.close();
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  public void addServer() {
    Thread t = new Thread(new Runnable() {
      public void run() {

        while (true) {
          try {
            Socket socket = serverInput.accept();
            entrada = new DataInputStream(socket.getInputStream());
            String nombre = entrada.readUTF();
            new Servidor(puertoDisponible, nombre).start();
            servidores.add(new ServerDTO(puertoDisponible, nombre, 0));
            puertoDisponible++;
            salida.close();
            socket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });

    t.start();
  }

  public static void main(String[] args) {
    ServerMaster server = new ServerMaster();
    server.start();
    server.addServer();
  }
}
