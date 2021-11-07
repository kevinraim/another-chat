import java.io.Serializable;
import java.net.Socket;
import java.util.Date;

public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  private Date horaDeConexion;
  private String nombre;
  private Socket socketServidor;
  private int cantidadDeSalas;


  public Usuario(String nombre) {
    this.nombre = nombre;
    this.horaDeConexion = new Date();
    this.cantidadDeSalas = 0;
  }

  public Date getHoraDeConexion() {
    return horaDeConexion;
  }

  public void setHoraDeConexion(Date horaDeConexion) {
    this.horaDeConexion = horaDeConexion;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Socket getSocketServidor() {
    return socketServidor;
  }

  public void setSocketServidor(Socket socketServidor) {
    this.socketServidor = socketServidor;
  }

  public int getCantidadDeSalas() {
    return cantidadDeSalas;
  }

  public void setCantidadDeSalas(int cantidadDeSalas) {
    this.cantidadDeSalas = cantidadDeSalas;
  }


}
