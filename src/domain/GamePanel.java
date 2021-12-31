package domain;

import domain.objects.Ball;
import domain.objects.Paddle;
import domain.objects.Ymir;
import domain.objects.obstacles.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private static final int DELAY = 10;
    private final int L = 1200;
    private final int H = 500;
    private final Controller controller = Controller.getInstance();
    private final Ymir ymir = Ymir.getInstance(3000);
    private Paddle paddle = Paddle.getInstance(L / 10, L / 2);
    private Timer timer;
    private boolean play = false;
    private int remainingLives = 3;

    private boolean pause = false;
    private Ball mainBall;


    public GamePanel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
        resetPositions();
        timer = new Timer(DELAY, this);
    }

    private void resetPositions() {
        paddle = Paddle.getInstance(L / 10, L / 2);
        mainBall = new Ball(16, 16, paddle.getX() - 8, paddle.getY() - 16, -1, -2);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, L, H);

        // obstacles
        ArrayList<Integer> positionsToRemove = new ArrayList<>();
        for (Integer pos : controller.obstacles.keySet()) {
            Obstacle obstacle = controller.obstacles.get(pos);

            int obstacleX = obstacle.getCoordinates().x;
            int obstacleY = obstacle.getCoordinates().y;
            int obstacleWidth = obstacle.getWidth();
            int obstacleHeight = 20;
            g2.setColor(obstacle.getColor());

            if (obstacle.getType().equals("explosive")) {
                g2.fillOval(obstacleX + obstacleWidth / 3, obstacleY + obstacleWidth / 4, obstacleWidth, obstacleWidth);
            } else g2.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            if (obstacle.getType().equals("firm")) {
                g2.setColor(Color.black);
                g2.drawString(String.valueOf(obstacle.getFirmness()), obstacleX + 8, obstacleY + 15);
            }

            Rectangle brickrect;
            if (obstacle.getType().equals("explosive")) {
                brickrect = new Rectangle(obstacleX, obstacleY, obstacleWidth, obstacleWidth);
            } else {
                brickrect = new Rectangle(obstacleX, obstacleY, obstacleWidth, obstacleHeight);
            }

            Rectangle ballrect = new Rectangle(mainBall.getBallposX(), mainBall.getBallposY(), 16, 16);
            
            if (ballrect.intersects(brickrect)) {
                obstacle.decreaseFirmness();
                if (obstacle.getFirmness() <= 0 && !obstacle.isFrozen()) {
                    positionsToRemove.add(pos);
                }
                if (mainBall.getBallposX() + 15 <= brickrect.x || mainBall.getBallposX() + 1 >= brickrect.x + obstacleWidth) {
                    mainBall.reverseDirX();
                } else {
                    mainBall.reverseDirY();
                }
            }
        }

        for (Integer posToRemove : positionsToRemove)
            controller.obstacles.remove(posToRemove);

        //the ball
        g2.setColor(Color.red);
        g2.fillOval(mainBall.getBallposX(), mainBall.getBallposY(), 16, 16);

        //the paddle
        g2.setColor(Color.BLUE);
        g2.rotate(Math.toRadians(paddle.getAngle()),(paddle.getX()), (paddle.getY()+ paddle.getHeight()));
        g2.fillRect(paddle.getX() - paddle.getWidth() / 2, paddle.getY(), paddle.getWidth(), paddle.getHeight());

        if(!play){
            g2.setFont(new Font("serif", Font.BOLD, 20));
            g2.drawString("Press W to shoot the ball", 500, 300);
        }

        if (play && pause) {
            g2.setFont(new Font("serif", Font.BOLD, 20));
            g2.drawString("Press P to continue", 500, 300);
        }

        g2.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play && !pause) {
            mainBall.move();
            ymir.updateRemainingTime(DELAY);

            if (new Rectangle(mainBall.getBallposX(), mainBall.getBallposY(), 15, 15).intersects(new Rectangle(paddle.getX() - 60, paddle.getY(), 120, 10))) {
                mainBall.reverseDirY();
            }
            if (mainBall.getBallposX() < 0) {
                mainBall.reverseDirX();
            }
            if (mainBall.getBallposY() < 0) {
                mainBall.reverseDirY();
            }
            if (mainBall.getBallposX() > L - 20) {
                mainBall.reverseDirX();
            }
            if (mainBall.getBallposY() > 470) {
                remainingLives--;
                if (remainingLives == 0) {
                    // todo öldükten sonraki menu vs
                    setVisible(false);
                }
                resetPositions();
                play = false;
                timer.stop();
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (play && !pause) {
                if (paddle.getX() >= L - 60) {
                    paddle.setX(L - 60);
                } else {
                    paddle.moveRight(L / 60);
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (play && !pause) {
                if (paddle.getX() <= 60) {
                    paddle.setX(60);
                } else {
                    paddle.moveLeft(L / 60);
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (!timer.isRunning()) {
                timer.start();
                play = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (timer.isRunning()) pause = !pause;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (timer.isRunning()) paddle.rotate("left");
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (timer.isRunning()) paddle.rotate("right");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
