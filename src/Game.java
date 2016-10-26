/**
 * Earl Timothy D. Malaki
 * BSCS - II | CMSC 22 - OOP
 * MP #4 - Pong
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

@SuppressWarnings("serial")
public class Game extends JPanel {

    // Game Instance objects and variables
    Ball ball;
    Racquet player1;
    Racquet player2;

    int gameSpeed;
    int scorePlayer1;
    int scorePlayer2;

    private final int fieldWidth = 400;
    private final int fieldHeight = 300;
    private final int fieldWidthHalf = fieldWidth / 2;
    private final int fieldHeightHalf = fieldHeight / 2;

    Random rand = new Random();


    /*** Constructor ***/
    public Game() {

        ball = new Ball();
        player1 = new Racquet(10);
        player2 = new Racquet(380);
        gameSpeed = 1;
        scorePlayer1 = 0;
        scorePlayer2 = 0;

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player1.setYa(0);
                player2.setYa(0);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //  Listen for inputs from player1
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    player1.setYa(-gameSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player1.setYa(gameSpeed);
                }

                //  Listen for inputs from player2
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    player2.setYa(-gameSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player2.setYa(gameSpeed);
                }
            }
        });
        setFocusable(true);
    }

    /*** End of Constructor ***/


    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Ping Pong");
        Game game = new Game();
        frame.add(game);
        frame.setSize(400, 300);
        frame.setLocation(400, 150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // game loop
        while (true) {
            game.requestFocus();// request focus for keylisteners
            game.move();        // object movements
            game.repaint();     // rendering
            Thread.sleep(10);   // pause thread
        }
    }


    /*** Methods ***/

    // Method for the movement of objects (Racquets and Ball)
    private void move() {

        // Ball handler
        boolean changeDirection = true;
        if (ball.getY() + ball.getYa() < 0) {    // hits UP side wall
            ball.setYa(gameSpeed);
        } else if (ball.getY() + ball.getYa() > fieldHeight - ball.getDIAMETER() - 20) {     // hits DOWN side wall   // const -20 is to fix the height difference
            ball.setYa(-gameSpeed);
        } else if (ball.getX() + ball.getXa() < 0) { // hits player 1 side, score for player 2
            gameSpeed = 1;
            ball.setXa(gameSpeed);
            ball.setYa(gameSpeed);
            ball.setX(fieldWidthHalf);
            ball.setY(fieldHeightHalf);
            scorePlayer2++;
            sleep();
            scoreCheck();
        } else if (ball.getX() + ball.getXa() > fieldWidth - ball.getDIAMETER()) {  // hits player 2 side, score for player 1
            gameSpeed = 1;
            ball.setXa(-gameSpeed);
            ball.setYa(-gameSpeed);
            ball.setX(fieldWidthHalf);
            ball.setY(fieldHeightHalf);
            scorePlayer1++;
            sleep();
            scoreCheck();
        } else if (collisionWith() == 1) {     // hit player 1 racquet
            if (powerUp() == 3) { // power up bounce
                ball.setXa(gameSpeed * 3);
            }
            else {  // normal bounce
                gameSpeed++;
                ball.setXa(gameSpeed);
            }
        } else if (collisionWith() == 2) {   // hit player 2 racquet
            if (powerUp() == 3) { // power up bounce
                ball.setXa(-gameSpeed * 3);
            }
            else { // normal bounce
                gameSpeed++;
                ball.setXa(-gameSpeed);
            }
        } else
            changeDirection = false;

        if (changeDirection)
            Sound.BALL.play();
        ball.setX(ball.getX() + ball.getXa());
        ball.setY(ball.getY() + ball.getYa());


        // Racquet handler
        moveRac(player1);
        moveRac(player2);

    }

    // Receives Racquet as player
    // Moves Racquet per call
    // Racquet coordinates are dependent on key listener
    public void moveRac(Racquet player) {
        if ((player.getY() + player.getYa() > 0) && (player.getY() + player.getYa() < fieldHeight - player.getHEIGHT() - 25)) {
            player.setY(player.getY() + player.getYa());
        }
    }

    // Method for painting/rendering the objects to the frame
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // render ball(oval), and racquets(player1 and player2)
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(player1.getX(), player1.getY(), player1.getWIDTH(), player1.getHEIGHT());
        g2d.fillRect(player2.getX(), player2.getY(), player2.getWIDTH(), player2.getHEIGHT());
        if (ball.getXa() == gameSpeed * 3 || ball.getXa() == -gameSpeed * 3){   // if POWER UP
            g2d.setColor(Color.PINK);       // pink ball means speed boost
        }
        g2d.fillOval(ball.getX(), ball.getY(), ball.getDIAMETER(), ball.getDIAMETER());


        g2d.setColor(Color.GRAY);
        g2d.drawLine(fieldWidthHalf, 0, fieldWidthHalf, fieldHeight);     // line to divide the field

        g2d.setFont(new Font("Verdana", Font.BOLD, 30));
        g2d.drawString(String.valueOf(getScore(1)), fieldWidthHalf - 35, fieldHeightHalf);   // display score of player 1
        g2d.drawString(String.valueOf(getScore(2)), fieldWidthHalf + 15, fieldHeightHalf);    // display score of player 2
    }


    // Handles Game Over prompt
    // Receives player as argument
    public void gameOver(int playerWinner) {
        Sound.GAMEOVER.play();
        if (playerWinner == 1) {
            JOptionPane.showMessageDialog(this, "Player 1 won!\nScore:  " + getScore(1), "Game Over", JOptionPane.YES_NO_OPTION);
        } else if (playerWinner == 2) {
            JOptionPane.showMessageDialog(this, "Player 2 won!\nScore:  " + getScore(2), "Game Over", JOptionPane.YES_NO_OPTION);
        }

        int choice = JOptionPane.showConfirmDialog(this, "Play again?", "Prompt", JOptionPane.YES_NO_OPTION);
        if (choice == 0) {   // 0 == YES in prompt. PLAY AGAIN
            playAgain();
        } else        // 1 == NO in prompt. Exit Game
            System.exit(ABORT);


    }

    // Checks score per point taken
    // Calls gameOver() accordingly
    public void scoreCheck() {
        if (scorePlayer1 == 3) {
            gameOver(1);
        } else if (scorePlayer2 == 3) {
            gameOver(2);
        }
    }

    // Check collision of ball.
    // Returns 1 if ball collided with player 1
    // Returns 2 if ball collided with player 2
    public int collisionWith() {
        if (player1.getBounds().intersects(ball.getBounds())) {
            return 1;
        } else if (player2.getBounds().intersects(ball.getBounds())) {
            return 2;
        }
        return 0;

    }

    // Returns score of received player
    private int getScore(int playerChoice) {
        if (playerChoice == 1)
            return scorePlayer1;
        return scorePlayer2;
    }

    // Reset the game to play again
    public void playAgain() {
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        gameSpeed = 1;
    }

    // Pause the thread for 1000milliseconds
    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Random power up
    // Power up is a ball bounce speed boost
    // A return of 3 means power up bounce, else normal bounce
    public int powerUp(){
        return rand.nextInt(4); // return random from 0 to 3
    }
    /*** Methods ***/

}