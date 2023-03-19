import javax.swing.*;
public class frame extends JFrame{
    frame() {
        //adding the panel to the frame
        this.add(new panel());
        this.setTitle("SnakeGame");
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }
}
