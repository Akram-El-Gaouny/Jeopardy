package Jeopardy;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Jeopardy {

    public static JFrame main = new JFrame("Jeopardy");
    public static int Swidth;
    public static int Sheight;
    public static int Score1 = 0;
    public static int Score2 = 0;
    public static int turn = 0;
    public static JLabel scores[] = new JLabel[2];
    public static Integer answers[][];
    public static String questions[][];
    public static JButton timer = new JButton("30");
    public static Thread ST[] = new Thread[20];//new Thread(new Time ());
    public static boolean g = false;
    public static JButton pressedbutton = new JButton();
  
    

    public static class Time implements Runnable {

        public void run() {
            AudioStream audioStream =null;
            try {
                timer.setText("30");
                g = false;
                String h =null;
                String effect1 = "sounds\\T.wav";// refers to the files
                InputStream sound1 = new FileInputStream(effect1);
                audioStream = new AudioStream(sound1);
                AudioPlayer.player.start(audioStream);
                for (int k = 30; k >= 0; k--) {
                    if (g == false) {
                        h = String.valueOf(k);
                        timer.setFont(new Font("Times New Roman", Font.PLAIN, 107));
                        timer.setText(h);
                        main.repaint();
                        
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } else {
                        timer.setText("30");
                    }
                }   if (h.equals("0")){
                    JOptionPane.getRootFrame().dispose();
                    timer.setText("30");
                    lose();}
            } catch (IOException ex) {
                Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    audioStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void lose() {
            int row = (int) pressedbutton.getClientProperty("row") - 1;
            int column = (int) pressedbutton.getClientProperty("column");
            AudioStream audioStream = null;
            try {
                String effect1 = "sounds\\B.wav";// refers to the files
                InputStream sound1 = new FileInputStream(effect1);
                audioStream = new AudioStream(sound1);
                AudioPlayer.player.start(audioStream);
                JOptionPane.showMessageDialog(null, "Times's UPPPP! The answer is " + answers[row][column]);
                pressedbutton.setBackground(Color.black);
                pressedbutton.setEnabled(false);
            } catch (IOException ex) {
                Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    audioStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            g = true;
        }
        
        
    }

    public static void main(String[] args) {
     
  
        FrameSettings();
        randomG();
        Jeopardy();

    }

    public static void FrameSettings() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Swidth = (Double.valueOf(screenSize.getWidth()).intValue()) - 50;
        Sheight = (Double.valueOf(screenSize.getHeight()).intValue()) - 50;
        System.out.print(Swidth + " " + Sheight);
        main.setSize(screenSize);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
        main.setLayout(null);
        Container c = main.getContentPane();
        c.setBackground(Color.darkGray);
        main.setVisible(true);

    }

    public static void Jeopardy() {

        ActionListener click;
        click = (ActionEvent e) -> {
            InputStream sound1 = null;
            try {
                String effect1 = "sounds\\CLICK.wav";// refers to the files
                sound1 = new FileInputStream(effect1);
                AudioStream audioStream = null;
                try {
                    audioStream = new AudioStream(sound1);
                } catch (IOException ex) {
                    Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
                }
                AudioPlayer.player.start(audioStream);
                ST[turn] = new Thread(new Time());
                ST[turn].start();
                turn++;
                if (e.getSource() instanceof JButton) {
                    pressedbutton = ((JButton) e.getSource());
                    int row = (int) pressedbutton.getClientProperty("row") - 1;
                    int column = (int) pressedbutton.getClientProperty("column");
                    int points = Integer.parseInt(pressedbutton.getText());
                    
                    int ans = Integer.parseInt(JOptionPane.showInputDialog(questions[row][column]));
                    
                    if (ans == answers[row][column]) {
                        win(points);
                        
                        
                    } else {
                        lose( row, column);
                    }
                    JOptionPane.showMessageDialog(null, "Pass the keyboard to the other player");
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    sound1.close();
                } catch (IOException ex) {
                    Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        int columns = 4;
        int rows = 6;

        JLabel Title = new JLabel("        Welcome To Jeopardy");
        Title.setForeground(Color.yellow);
        Title.setFont(new Font("Times New Roman", Font.PLAIN, 107));
        Title.setLocation(0, 0);
        Title.setSize(Swidth, (int) (0.15 * Sheight));
        main.add(Title);
        JPanel Board = new JPanel();
        Board.setSize((int) (0.75 * Swidth), (int) (0.85 * Sheight));
        Board.setLocation(0, (int) (0.155 * Sheight));
        Board.setLayout(new GridLayout(rows, columns));
        Board.setVisible(true);
        main.add(Board);
        JButton[][] boxes = new JButton[rows][columns];

        for (int g = 0; g < rows; g++) {
            for (int z = 0; z < columns; z++) {
                boxes[g][z] = new JButton();
                boxes[g][z].setSize(100, 100);
                boxes[g][z].setVisible(true);
                boxes[g][z].putClientProperty("column", z);
                boxes[g][z].putClientProperty("row", g);
                boxes[g][z].addActionListener(click);
                Board.add(boxes[g][z]);

            }
        }

        String Categories[] = new String[]{"Addition", "Subtraction", "Multiplication", "Random"};
        String[] points = new String[]{"100", "200", "300", "400", "500"};
        for (int y = 0; y < columns; y++) {
            boxes[0][y].setFont(new Font("Pacifo", Font.PLAIN, 35));
            boxes[0][y].setText(Categories[y]);
            boxes[0][y].setBackground(Color.blue);

            boxes[0][y].setEnabled(false);
            boxes[0][y].setForeground(new Color(255, 255, 25));
        }

        Font F = new Font("Calibri", Font.PLAIN, 30);
        Color Y = new Color(255, 255, 25);
        Color B = new Color(255, 25, 59);
        for (int p = 1; p < rows; p++) {
            for (int k = 0; k < columns; k++) {

                boxes[p][k].setFont(F);
                boxes[p][k].setText(points[p - 1]);
                boxes[p][k].setForeground(Y);
                boxes[p][k].setBackground(B);

            }
        }
        JLabel players[] = new JLabel[2];
        scores[0] = new JLabel(String.valueOf(Score1));
        scores[1] = new JLabel(String.valueOf(Score2));

        for (int f = 0; f < scores.length; f++) {
            players[f] = new JLabel("Player " + (f + 1));
            players[f].setSize((int) (0.25 * Swidth), (int) (0.14 * Sheight));
            players[f].setFont(new Font("Times New Roman", Font.PLAIN, 45));
            players[f].setForeground(Color.yellow);
            main.add(players[f]);
            scores[f].setSize((int) (0.25 * Swidth), (int) (0.21 * Sheight));
            scores[f].setVisible(true);
            scores[f].setFont(new Font("Times New Roman", Font.PLAIN, 50));

            main.add(scores[f]);

        }
        players[0].setLocation((int) (0.83 * Swidth), (int) (Sheight - (0.60 * Sheight)));
        players[1].setLocation((int) (0.83 * Swidth), (int) (Sheight - (0.35 * Sheight)));
        scores[0].setLocation((int) (0.83 * Swidth), (int) (Sheight - (0.50 * Sheight)));
        scores[0].setForeground(Color.blue);
        scores[1].setLocation((int) (0.83 * Swidth), (int) (Sheight - (0.24 * Sheight)));
        scores[1].setForeground(Color.red);

        timer.setSize((int) (0.16 * Swidth), (int) (0.19 * Sheight));
        timer.setLocation((int) (0.82 * Swidth), 0);
        timer.setFont(new Font("Times New Roman", Font.PLAIN, 107));
        main.add(timer);
        main.repaint();

    }

    public static void randomG() {
        int rows = 5;
        int columns = 4;

        Integer FRandom[][] = new Integer[rows][columns];
        Integer SRandom[][] = new Integer[rows][columns];
        answers = new Integer[rows][columns];
        questions = new String[rows][columns];
        Random G = new Random();
        for (int k = 0; k < columns; k++) {
            for (int p = 0; p < rows; p++) {
                if (p == 0) {
                    FRandom[p][k] = G.nextInt(20) + 1;
                    SRandom[p][k] = G.nextInt(20) + 1;

                } else if (p == 1) {
                    FRandom[p][k] = G.nextInt(35) + 20;
                    SRandom[p][k] = G.nextInt(35) + 20;
                } else if (p == 2) {
                    FRandom[p][k] = G.nextInt(20) + 55;
                    SRandom[p][k] = G.nextInt(20) + 55;
                } else if (p == 3) {
                    FRandom[p][k] = G.nextInt(20) + 75;
                    SRandom[p][k] = G.nextInt(20) + 75;
                } else if (p == 4) {
                    FRandom[p][k] = G.nextInt(40) + 92;
                    SRandom[p][k] = G.nextInt(40) + 110;
                }

                if (k == 0) {
                    answers[p][k] = FRandom[p][k] + SRandom[p][k];
                    questions[p][k] = "What is " + FRandom[p][k] + " + " + SRandom[p][k];
                } else if (k == 1) {
                    answers[p][k] = FRandom[p][k] - SRandom[p][k];
                    questions[p][k] = "What is " + FRandom[p][k] + " - " + SRandom[p][k];
                } else if (k == 2) {
                    answers[p][k] = FRandom[p][k] * SRandom[p][k];
                    questions[p][k] = "What is " + FRandom[p][k] + " * " + SRandom[p][k];
                } else if (k == 3) {
                    Random f = new Random();
                    int selecto = f.nextInt(3);
                    if (selecto == 0) {
                        answers[p][k] = (FRandom[p][k]) + SRandom[p][k];
                        questions[p][k] = "What is " + FRandom[p][k] + " + " + SRandom[p][k];
                    } else if (selecto == 1) {
                        answers[p][k] = (FRandom[p][k]) - SRandom[p][k];
                        questions[p][k] = "What is " + FRandom[p][k] + " - " + SRandom[p][k];
                    } else if (selecto == 2) {
                        answers[p][k] = (FRandom[p][k]) * SRandom[p][k];
                        questions[p][k] = "What is " + FRandom[p][k] + " * " + SRandom[p][k];
                    }
                }
            }
        }

    }

    public static void win( int points) {
        InputStream sound1 = null;
        try {
            String effect1 = "sounds\\C.wav";// refers to the files
            sound1 = new FileInputStream(effect1);
            AudioStream audioStream = new AudioStream(sound1);
            AudioPlayer.player.start(audioStream);
            JOptionPane.showMessageDialog(null, "Correct! you've earned " + points + " points");
            pressedbutton.setEnabled(false);
            if (turn % 2 == 1) {
                Score1 = Score1 + points;
                pressedbutton.setBackground(Color.green);
                scores[0].setText(String.valueOf(Score1));
            } else if (turn % 2 == 0) {
                Score2 = Score2 + points;
                pressedbutton.setBackground(Color.CYAN);
                scores[1].setText(String.valueOf(Score2));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                sound1.close();
            } catch (IOException ex) {
                Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        g = true;
    }

    public static void lose( int row, int column) {
        AudioStream audioStream = null;
        try {
            String effect1 = "sounds\\B.wav";// refers to the files
            InputStream sound1 = new FileInputStream(effect1);
            audioStream = new AudioStream(sound1);
            AudioPlayer.player.start(audioStream);
            JOptionPane.showMessageDialog(null, "Wrong the answer is " + answers[row][column]);
            pressedbutton.setBackground(Color.black);
            pressedbutton.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                audioStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Jeopardy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        g = true;
    }
}
