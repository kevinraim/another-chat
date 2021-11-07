import java.io.Serializable;

public class ServerDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private String nombre;
  private int puerto;
  private int cantidadConectados;

  public ServerDTO(int puerto, String nombre, int cantidadConectados) {
    this.nombre = nombre;
    this.puerto = puerto;
    this.cantidadConectados = cantidadConectados;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getPuerto() {
    return puerto;
  }

  public void setPuerto(int puerto) {
    this.puerto = puerto;
  }

  public int getCantidadConectados() {
    return cantidadConectados;
  }

  public void setCantidadConectados(int cantidadConectados) {
    this.cantidadConectados = cantidadConectados;
  }


}
