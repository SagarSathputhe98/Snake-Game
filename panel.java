import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;

public class panel extends JPanel implements ActionListener {
    // Declare static variables for the width, height, and size of each game unit
    static int width = 1200;
    static int height = 600;
    static int unit = 50;

    // Declare a Timer object for the game loop
    Timer timer;

    // Declare a Random object to randomly determine the location of food
    Random random;

    // Declare variables for the x and y coordinates of the food, the player's
    // score, and the player's length
    int foodx, foody;
    int score;
    int length = 3;

    // Declare a variable for the direction the player is moving
    char dir = 'R';

    // Declare a flag to indicate if the game is over or not
    boolean flag = false;

    // Declare a delay for the game loop
    static int delay = 160;

    // Declare arrays for the x and y coordinates of the snake's body segments
    int xsnake[] = new int[288];
    int ysnake[] = new int[288];

    // Constructor for the panel
    panel() {

        // Set the preferred size of the panel to the width and height of the game
        // screen
        this.setPreferredSize(new Dimension(width, height));

        // Set the background color of the panel to black
        this.setBackground(Color.black);

        // Allow the panel to receive keyboard input
        this.setFocusable(true);

        // Create a new Random object
        random = new Random();

        // Add a KeyListener to the panel to listen for input from the player
        this.addKeyListener(new mykey());

        // Start the game
        gamestart();
    }

    // Method to start the game
    public void gamestart() {

        // Spawn the first food item randomly on the screen
        spawnfood();

        // Set the flag to indicate that the game is running
        flag = true;

        // Start the timer to update the game loop
        timer = new Timer(delay, this);
        timer.start();
    }

    // Method to randomly spawn a food item on the screen
    public void spawnfood() {
        foodx = random.nextInt(width / unit) * unit;
        foody = random.nextInt(height / unit) * unit;
    }

    // Override the paintComponent method to draw the game screen
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }

    // Method to draw the game screen
    public void draw(Graphics graphic) {

        // If the game is still running, draw the snake, food, and score
        if (flag == true) {
            graphic.setColor(Color.RED);
            graphic.fillOval(foodx, foody, unit, unit);

            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    graphic.setColor(Color.orange);
                } else {
                    graphic.setColor(Color.GREEN);
                }

                graphic.fillRect(xsnake[i], ysnake[i], unit, unit);
            }

            graphic.setColor(Color.cyan);
            graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
            FontMetrics f = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:" + score, (width - f.stringWidth("Score:" + score)) / 2,
                    graphic.getFont().getSize());
        } else { // If the game is over, display the score and game over message
            gameover(graphic);
        }
    }

    public void gameover(Graphics graphic) {
        // to display the score
        graphic.setColor(Color.cyan);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
        FontMetrics f = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:" + score, (width - f.stringWidth("Score:" + score)) / 2,
                graphic.getFont().getSize());

        // to display the gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 80));
        FontMetrics f2 = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER!", (width - f2.stringWidth("GAME OVER!")) / 2, height / 2);

        // to display the replay prompt
        graphic.setColor(Color.green);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
        graphic.drawString("Press R to replay", (width - f.stringWidth("Press R to replay")) / 2, height / 2 + 150);

    }

    public void checkhit() {
        if (xsnake[0] < 0) {
            flag = false;
        } else if (xsnake[0] > 1200) {
            flag = false;
        } else if (ysnake[0] < 0) {
            flag = false;
        } else if (ysnake[0] > 600) {
            flag = false;
        }
        for (int i = length; i > 0; i--) {
            if ((xsnake[0] == xsnake[i]) && (ysnake[0] == ysnake[i])) {
                flag = false;
            }
        }
        if (!flag) {
            timer.stop();
        }
    }

    public void eat() {
        if ((xsnake[0] == foodx) && (ysnake[0] == foody)) {
            length++;
            score++;
            spawnfood();
        }
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            xsnake[i] = xsnake[i - 1];
            ysnake[i] = ysnake[i - 1];
        }
        switch (dir) {
            case 'R':
                xsnake[0] = xsnake[0] + unit;
                break;
            case 'L':
                xsnake[0] = xsnake[0] - unit;
                break;
            case 'D':
                ysnake[0] = ysnake[0] + unit;
                break;
            case 'U':
                ysnake[0] = ysnake[0] - unit;
                break;
        }
    }

    public class mykey extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (dir != 'D') {
                        dir = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dir != 'U') {
                        dir = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (dir != 'R') {
                        dir = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dir != 'L') {
                        dir = 'R';
                    }
                    break;
                case KeyEvent.VK_R:
                    if (!flag) {
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake, 0);
                        Arrays.fill(ysnake, 0);

                        gamestart();
                    }
                    break;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (flag) {
            move();
            eat();
            checkhit();
        }
        repaint();
    }
}
