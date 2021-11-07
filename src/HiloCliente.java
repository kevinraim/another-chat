import java.io.DataInputStream;
import java.io.IOException;

public class HiloCliente extends Thread {

  private static final String HORA_DE_CONEXION_KEY = "HORAS_DE_CONEXION";
  private static final String CIERRE_DE_CONEXION = "CIERRE_DE_CONEXION";
  private DataInputStream entrada;
  private JChatCliente ventana;
  private boolean running = true;

  public HiloCliente(DataInputStream entrada, JChatCliente ventana) {

    this.entrada = entrada;
    this.ventana = ventana;
  }

  public void run() {

    String mensaje;

    try {

      while (running) {

        mensaje = this.entrada.readUTF();

        if (mensaje.equals(CIERRE_DE_CONEXION)) {
          this.cerrarConexion();
        }

        else if (mensaje.startsWith(HORA_DE_CONEXION_KEY)) {
          this.ventana.escribirHorasDeConexcion(mensaje.substring(HORA_DE_CONEXION_KEY.length()));
        }

        else {
          this.ventana.escribirMensajeEnTextArea(mensaje);
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void cerrarConexion() {
    this.running = false;
    try {
      this.entrada.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
