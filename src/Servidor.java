import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {

  private String nombre;
  private int puerto;
  private ServerSocket server;
  private ArrayList<Usuario> usuarios;
  private Usuario usuario;
  private ObjectInputStream object;
  private int cantidadConectados;

  public Servidor(int puerto, String nombre) {

    this.nombre = nombre;
    this.cantidadConectados = 0;
    this.puerto = puerto;
  }

  public void run() {
    try {
      server = new ServerSocket(puerto);
      usuarios = new ArrayList<Usuario>();

    } catch (IOException e) {
      e.printStackTrace();
    }

    Socket socket;
    int i = 0;

    while (i < 100) {

      try {
        socket = server.accept();
        object = new ObjectInputStream(socket.getInputStream());
        usuario = (Usuario) object.readUnshared();
        cantidadConectados++;
        usuario.setSocketServidor(socket);
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      usuarios.add(usuario);
      new HiloServidor(usuario, usuarios).start();
      i++;
    }
  }

  public String getNombre() {
    return this.nombre;
  }

  public int getPuerto() {
    return this.puerto;
  }

  public int getCantidadConectados() {
    return this.cantidadConectados;
  }

  public void setCantidadConectados(int conectados) {
    this.cantidadConectados = conectados;
  }

  private synchronized void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

}
