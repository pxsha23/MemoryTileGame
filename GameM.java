import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GameM implements ActionListener {
    JFrame frame = new JFrame("Memory Game");
    JPanel field = new JPanel();
    JPanel start_screen = new JPanel();
    JButton[] buttons = new JButton[9];
    JButton startButton = new JButton("Start");
    JButton exitButton = new JButton("Exit");
    Random randomGenerator = new Random();
    Boolean gameOver = false;
    int revealedFirst = -1;
    int revealedSecond = -1;
    boolean checkForMatch = false;
    String[] symbols;
    String[] boardSymbols;

    public GameM() {
        frame.setSize(400, 300);
        frame.setLocation(500, 300);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start_screen.setLayout(new BorderLayout());

        startButton.addActionListener(this);
        exitButton.addActionListener(this);
        
        JPanel menuPanel = new JPanel();
        menuPanel.add(startButton);
        menuPanel.add(exitButton);

        start_screen.add(menuPanel, BorderLayout.SOUTH);
        frame.add(start_screen, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void initializeGame() {
        clearMainMenu();
        setupBoard();
        populateSymbols();
        revealBoard();
    }

    private void setupBoard() {
        field.setLayout(new GridLayout(3, 3, 2, 2));
        field.setBackground(Color.BLACK);
        
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setBackground(new Color(220, 220, 220));
            buttons[i].addActionListener(this);
            buttons[i].setEnabled(true);
            field.add(buttons[i]);
        }

        start_screen.add(field, BorderLayout.CENTER);
        start_screen.revalidate();
        start_screen.repaint();
    }

    private void populateSymbols() {
        symbols = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        boardSymbols = new String[9];
        
        for (int i = 0; i < 4; i++) {
            int pos1, pos2;
            String symbol = symbols[i];
            do {
                pos1 = randomGenerator.nextInt(9);
            } while (boardSymbols[pos1] != null);
            do {
                pos2 = randomGenerator.nextInt(9);
            } while (boardSymbols[pos2] != null || pos2 == pos1);

            boardSymbols[pos1] = symbol;
            boardSymbols[pos2] = symbol;
        }
    }

    public void hideBoard() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
        }
    }

    public void switchButton(int index) {
        if (buttons[index].getText().isEmpty()) {
            buttons[index].setText(boardSymbols[index]);
        } else {
            buttons[index].setText("");
        }
    }

    public void revealBoard() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setText(boardSymbols[i]);
        }
        Timer timer = new Timer(2000, e -> hideBoard());
        timer.setRepeats(false);
        timer.start();
    }

    private void displayWinMessage() {
        JOptionPane.showMessageDialog(frame, "You Win!");
        resetGame();
    }

    private void displayLoseMessage() {
        JOptionPane.showMessageDialog(frame, "You Lose!");
        resetGame();
    }

    public void checkForWin() {
        for (String symbol : boardSymbols) {
            if (symbol != null && !symbol.equals("matched")) {
                return;
            }
        }
        displayWinMessage();
    }

    public void clearMainMenu() {
        start_screen.removeAll();
        start_screen.add(field, BorderLayout.CENTER);
    }

    private void resetGame() {
        frame.dispose();
        new GameM();
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        Object source = click.getSource();
        
        if (source == startButton) {
            initializeGame();
        } else if (source == exitButton) {
            System.exit(0);
        } else {
            for (int i = 0; i < 9; i++) {
                if (source == buttons[i]) {
                    if (revealedFirst == -1) {
                        revealedFirst = i;
                        switchButton(i);
                    } else if (revealedSecond == -1 && i != revealedFirst) {
                        revealedSecond = i;
                        switchButton(i);
                        
                        if (boardSymbols[revealedFirst].equals(boardSymbols[revealedSecond])) {
                            boardSymbols[revealedFirst] = "matched";
                            boardSymbols[revealedSecond] = "matched";
                            revealedFirst = -1;
                            revealedSecond = -1;
                            checkForWin();
                        } else {
                            Timer timer = new Timer(500, e -> {
                                switchButton(revealedFirst);
                                switchButton(revealedSecond);
                                revealedFirst = -1;
                                revealedSecond = -1;
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameM();
    }
}


