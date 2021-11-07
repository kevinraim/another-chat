import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Utils.JButtonServerComparator;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Lobby extends JFrame {

  private JPanel contentPane;
  private JTextField textNombreSalaNueva;

  private List<JButton> servidoresBoton;
  private List<Servidor> servidores;

  private Usuario usuario;

  private Socket socket;

  private ActionListener listenerServidores = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {

      if (usuario == null || usuario.getNombre().isBlank()) {
        JOptionPane.showMessageDialog(Lobby.this, "Ingrese un nombre de usuario");
        return;
      }

      if (usuario.getCantidadDeSalas() >= 3) {
        JOptionPane.showMessageDialog(Lobby.this, "Maximo de salas alcanzado");
        return;
      }

      int index = JButtonServerComparator.getIndexOf(servidoresBoton, (JButton) e.getSource());
      JChatCliente chatCliente = new JChatCliente();
      chatCliente.setPuerto(servidores.get(index).getPuerto());

      usuario.setCantidadDeSalas(usuario.getCantidadDeSalas() + 1);
      chatCliente.setUsuario(usuario);
      chatCliente.iniciarChat();
      chatCliente.setTitle("Sala: " + servidores.get(index).getNombre());
      chatCliente.setVisible(true);
    }
  };

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Lobby frame = new Lobby();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public Lobby() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnOpciones = new JMenu("Opciones");
    menuBar.add(mnOpciones);

    JMenuItem mntmConectar = new JMenuItem("Conectar");
    mnOpciones.add(mntmConectar);

    mntmConectar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String nombreUsuario =
            JOptionPane.showInputDialog(Lobby.this, "Conectarse", "Nombre de usuario", 1);
        setTitle("Chat - " + nombreUsuario);
        if (nombreUsuario != null) {
          usuario = new Usuario(nombreUsuario);
        }
      }
    });



    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    servidoresBoton = new ArrayList<JButton>();
    servidores = new ArrayList<Servidor>();

    JButton btnCrearSala = new JButton("Crear sala");
    btnCrearSala.setBounds(214, 211, 121, 20);
    contentPane.add(btnCrearSala);

    btnCrearSala.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (textNombreSalaNueva.getText().isBlank()
            || textNombreSalaNueva.getText().contains(" ")) {
          JOptionPane.showMessageDialog(Lobby.this, "Nombre de sala inválido");
          return;
        }
        addServer(textNombreSalaNueva.getText());
        mostrarServidores();
      }
    });

    textNombreSalaNueva = new JTextField();
    textNombreSalaNueva.setBounds(84, 212, 121, 19);
    contentPane.add(textNombreSalaNueva);
    textNombreSalaNueva.setColumns(10);

    JButton btnRefrescar = new JButton("Refrescar");
    btnRefrescar.setBounds(323, 21, 103, 27);
    contentPane.add(btnRefrescar);

    btnRefrescar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mostrarServidores();
      }
    });

    mostrarServidores();
  }

  public void mostrarServidores() {
    this.servidoresBoton = null;
    this.servidoresBoton = new ArrayList<JButton>();

    getServers();

    if (servidores.size() == 0)
      return;

    for (Servidor servidor : servidores) {
      JButton button = new JButton(servidor.getNombre() + " - " + servidor.getCantidadConectados());
      button.setName(String.valueOf(servidor.getPuerto()));
      button.addActionListener(listenerServidores);
      servidoresBoton.add(button);
    }

    int x = 107, y = 25, width = 120, heigth = 21;

    for (JButton button : servidoresBoton) {
      button.setBounds(x, y, width, heigth);
      contentPane.add(button);

      y += 30;
    }

    this.repaint();
  }

  public void getServers() {
    try {
      socket = new Socket("127.0.0.1", 50000);
      ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
      List<ServerDTO> serverDTO = (List<ServerDTO>) entrada.readObject();
      entrada.close();
      dtoToServidor(serverDTO);
    } catch (IOException e) {
      e.printStackTrace();
      this.servidores = new ArrayList<Servidor>();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      this.servidores = new ArrayList<Servidor>();
    }

  }

  public void dtoToServidor(List<ServerDTO> serversDTO) {

    this.servidores = null;
    this.servidores = new ArrayList<Servidor>();
    for (ServerDTO serverDTO : serversDTO) {
      servidores.add(new Servidor(serverDTO.getPuerto(), serverDTO.getNombre()));
    }
  }

  public void addServer(String nombre) {
    try {
      socket = new Socket("127.0.0.1", 50001);
      DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
      salida.writeUTF(nombre);
      salida.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
