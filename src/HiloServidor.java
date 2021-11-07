import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HiloServidor extends Thread {

  private static final String HORA_DE_CONEXION_KEY = "HORAS_DE_CONEXION";
  private static final String CIERRE_DE_CONEXION = "CIERRE_DE_CONEXION";
  private ArrayList<Usuario> usuarios;
  private Usuario usuario;
  private boolean running = true;

  public HiloServidor(Usuario usuario, ArrayList<Usuario> usuarios) {
    this.usuario = usuario;
    this.usuarios = usuarios;
  }

  public void run() {

    DataInputStream entrada;
    DataOutputStream salida;
    String mensaje;

    try {
      entrada = new DataInputStream(this.usuario.getSocketServidor().getInputStream());

      while (running) {

        mensaje = entrada.readUTF();

        if (mensaje.contains(CIERRE_DE_CONEXION)) {
          running = false;
          salida = new DataOutputStream(this.usuario.getSocketServidor().getOutputStream());
          salida.writeUTF(CIERRE_DE_CONEXION);
          entrada.close();
          this.usuario.getSocketServidor().close();
          this.usuarios.remove(usuario);

          for (Usuario envio : usuarios) {
            salida = new DataOutputStream(envio.getSocketServidor().getOutputStream());
            salida.writeUTF(usuario.getNombre() + " se ha desconectado");
          }
        }

        else if (mensaje.equals(HORA_DE_CONEXION_KEY)) {
          String horasDeConexion = HORA_DE_CONEXION_KEY + System.lineSeparator();
          for (Usuario usuario : usuarios) {
            long diff = new Date().getTime() - usuario.getHoraDeConexion().getTime();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
            long hours = TimeUnit.MILLISECONDS.toHours(diff);

            horasDeConexion += usuario.getNombre() + ": " + hours + "H " + minutes + "M " + seconds
                + "S " + System.lineSeparator();
          }
          salida = new DataOutputStream(this.usuario.getSocketServidor().getOutputStream());
          salida.writeUTF(horasDeConexion);

        } else {
          for (Usuario envio : usuarios) {
            salida = new DataOutputStream(envio.getSocketServidor().getOutputStream());
            salida.writeUTF(mensaje);
          }
        }
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
