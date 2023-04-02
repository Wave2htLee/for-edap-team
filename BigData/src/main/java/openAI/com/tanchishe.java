package openAI.com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class tanchishe extends JFrame implements KeyListener, ActionListener {
    private static final long serialVersionUID = 1L;

    private int width = 30;
    private int height = 30;
    private int size = 20;
    private int[][] matrix = new int[width][height];
    private int[] snakeX = new int[900];
    private int[] snakeY = new int[900];
    private int snakeLength = 3;
    private int foodX;
    private int foodY;
    private boolean isStarted = false;
    private boolean isFailed = false;
    private Timer timer;

    public tanchishe() {
        init();
        this.setTitle("贪吃蛇");
        this.setSize(width * size, height * size);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(this);
        timer = new Timer(100, this);
        timer.start();
    }

    public void init() {
        snakeX[0] = 2;
        snakeY[0] = 2;
        snakeX[1] = 1;
        snakeY[1] = 2;
        snakeX[2] = 0;
        snakeY[2] = 2;
        foodX = 20;
        foodY = 20;
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.setBackground(Color.WHITE);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (matrix[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(i * size, j * size, size, size);
                } else if (matrix[i][j] == 2) {
                    g.setColor(Color.RED);
                    g.fillOval(i * size, j * size, size, size);
                }
            }
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString("得分：" + (snakeLength - 3) * 10, 10, 60);
        if (!isStarted) {
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("Press Space to Start", 150, 300); // Fill in ${INSERT_HERE} with this line of code
            // go on
            if (isFailed) {
                g.setColor(Color.RED);
                g.setFont(new Font("微软雅黑", Font.BOLD, 40));
                g.drawString("Game Over! Press Space to Restart", 100, 300);
            }        // go on
        }// go on
    }

    public static void main(String[] args) {
        new tanchishe();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            if (isFailed) {
                isFailed = false;
                init();
            } else {
                isStarted = !isStarted;
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStarted && !isFailed) {
            // Update snake position and check for collisions
        }
        repaint();
    }
}