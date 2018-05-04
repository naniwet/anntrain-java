import javax.swing.*;
import java.awt.*;

public class ui1 {
    private JPanel jp0;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JList list1;
    private JPanel jp1;
    private JPanel jp11;
    private JPanel jp12;
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    private JFrame frame;

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setExtendedState( Frame.MAXIMIZED_BOTH );
        ui1 test = new ui1();
        ImageIcon icon=new ImageIcon("C:\\Users\\gpsts1\\Desktop\\PCB_ANN\\res\\train\\data\\4\\test\\6.jpg");

        icon.setImage(icon.getImage().getScaledInstance(700,700,Image.SCALE_DEFAULT));
        frame.setContentPane(test.scroll1);
        test.label1.setSize(700,700);
        test.label2.setSize(700,700);
        test.label1.setIcon(icon);
        test.label2.setIcon(icon);
//        frame.pack();
        frame.setSize(1080,810);
        frame.setResizable(true);
        frame.setVisible( true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }
}
