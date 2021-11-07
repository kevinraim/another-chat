import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {

  private static final String HORA_DE_CONEXION_KEY = "HORAS_DE_CONEXION";
  private static final String CIERRE_DE_CONEXION = "CIERRE_DE_CONEXION";
  private Socket socket;
  private int puerto;
  private String ip;
  private DataInputStream entrada;
  private DataOutputStream salida;
  private Usuario usuario;
  private HiloCliente hiloCliente;

  public Cliente(int puerto, String ip, Usuario usuario) {

    this.puerto = puerto;
    this.ip = ip;
    this.usuario = usuario;

    try {
      this.socket = new Socket(this.ip, this.puerto);
      this.salida = new DataOutputStream(this.socket.getOutputStream());
      this.entrada = new DataInputStream(this.socket.getInputStream());

      ObjectOutputStream objecto = new ObjectOutputStream(this.socket.getOutputStream());
      objecto.writeUnshared(this.usuario);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void enviarMensaje(String mensaje) {

    try {
      this.salida.writeUTF(mensaje);
      // this.socket.close();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void inicializarHiloCliente(JChatCliente ventana) {
    hiloCliente = new HiloCliente(this.entrada, ventana);
    hiloCliente.start();
  }

  public void solicitarHorasDeConexion() {
    try {
      this.salida.writeUTF(HORA_DE_CONEXION_KEY);
      // this.socket.close();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void cerrarConexion() {
    try {
      this.salida.writeUTF(CIERRE_DE_CONEXION);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
