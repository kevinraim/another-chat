package Utils;

import java.util.List;
import javax.swing.JButton;

public class JButtonServerComparator {

  public static int getIndexOf(List<JButton> buttons, JButton other) {
    for (int i = 0; i < buttons.size(); i++) {
      if (buttons.get(i).getName().equals(other.getName())) {
        return i;
      }
    }

    return -1;
  }
}
