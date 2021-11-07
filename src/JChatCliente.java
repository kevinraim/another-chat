import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
// import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
// import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.Color;
// import java.awt.Component;

// import javax.swing.SwingConstants;
// import java.awt.Window.Type;
// import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
// import java.io.IOException;
import java.awt.event.ActionEvent;


public class JChatCliente extends JFrame {

  private JPanel contentPane;
  private JTextField textField;
  private JMenuBar menuBar;
  private JMenu mnOpciones;
  private JTextArea textArea;
  private JButton btnEnviar;
  private Cliente cliente;
  private Usuario usuario;
  private JMenuItem mntmVerTiempo;
  private JMenuItem mntmDescargarChat;
  private int puerto;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          JChatCliente frame = new JChatCliente();
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
  public JChatCliente() {
    setResizable(false);
    setForeground(Color.WHITE);
    setBackground(Color.WHITE);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 450, 325);

    menuBar = new JMenuBar();
    menuBar.setMargin(new Insets(1, 1, 1, 1));
    setJMenuBar(menuBar);

    mnOpciones = new JMenu("Opciones");
    menuBar.add(mnOpciones);

    mntmVerTiempo = new JMenuItem("Ver tiempos de conexion");
    mnOpciones.add(mntmVerTiempo);

    mntmDescargarChat = new JMenuItem("Descargar chat");
    mnOpciones.add(mntmDescargarChat);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    setContentPane(contentPane);
    contentPane.setLayout(null);

    mntmVerTiempo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cliente.solicitarHorasDeConexion();
      }
    });

    mntmDescargarChat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String path =
            JOptionPane.showInputDialog(JChatCliente.this, "Path", "Ingrese la ubicacion", 1);
        descargarChat(path);
      }
    });

    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setForeground(new Color(0, 0, 0));
    textArea.setBounds(10, 11, 414, 195);
    contentPane.add(textArea);

    textField = new JTextField();
    textField.setBounds(10, 217, 282, 31);
    contentPane.add(textField);
    textField.setColumns(10);

    btnEnviar = new JButton("Enviar");
    btnEnviar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {

        String mensaje =
            usuario.getNombre() + ": " + textField.getText() + System.lineSeparator() + new Date();
        cliente.enviarMensaje(mensaje);
        textField.setText("");
      }
    });
    btnEnviar.setBounds(335, 217, 89, 31);
    contentPane.add(btnEnviar);

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        usuario.setCantidadDeSalas(usuario.getCantidadDeSalas() - 1);
        cliente.cerrarConexion();
      }
    });
  }

  public void escribirMensajeEnTextArea(String mensaje) {

    textArea.append(mensaje + "\n");
  }

  public void escribirHorasDeConexcion(String mensaje) {
    JOptionPane.showMessageDialog(JChatCliente.this, mensaje);
  }

  public void descargarChat(String path) {
    try {
      String ruta = path + ".txt";
      File file = new File(ruta);

      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fw = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(textArea.getText());
      bw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setPuerto(int puerto) {
    this.puerto = puerto;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void iniciarChat() {
    cliente = new Cliente(puerto, "localhost", usuario);
    cliente.inicializarHiloCliente(JChatCliente.this);
  }
}
