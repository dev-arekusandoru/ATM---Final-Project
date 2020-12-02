import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author: Alexandru Muresan
 **/

// custom button class, subclass of JButton
public class Button extends JButton {
    //
    public Button(String text, int w, int h) {
        // create a button
        super(text);
        // set cursor to hand when over
        // and default when out
        super.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        // set size to preffered size,
        // set background to blue,
        // set text to white
        // set border empty
        super.setPreferredSize(new Dimension(w, h));
        super.setOpaque(true);
        super.setBackground(new Color(0, 80, 157));
        super.setForeground(ATM.themeC);
        super.setBorder(new EmptyBorder(0, 0, 0, 0));

    }

    // set the cursor to the desired state
    public void setCursor(Cursor aCursor) {
        super.setCursor(aCursor);
    }

}
