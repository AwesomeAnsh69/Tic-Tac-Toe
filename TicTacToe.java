import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Main class for the Tic-Tac-Toe game
public class TicTacToe {

    // Dimensions of the game board
    int boardWidth = 600;
    int boardHeight = 650; // Allocating extra 50 pixels for the text panel at the top

    // Players and their symbols
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;          // The current player (starts with "X")

    // Game state variables
    boolean gameOver = false;                // Flag to indicate if the game is over
    int turns = 0;                           

    int playerXWins = 0;        //tracker for the number of times playerX has won
    int playerOWins = 0;        //tracker for the number of times playerO has won
    int numberOfTies = 0;

    // GUI components
    JFrame frame = new JFrame("Tic-Tac-Toe"); 
    JLabel textLabel, playerXWinsLabel, playerOWinsLabel, tiesLabel;
    JPanel textPanel, boardPanel, bottomPanel, exitPanel, restartPanel, resetPanel, playerXWinsPanel, playerOWinsPanel, tiesPanel;
    JButton exitButton, resetButton, restartButton;

    // 3x3 grid of buttons for the game board
    JButton[][] board = new JButton[3][3];

    // Constructor to set up the game
    public TicTacToe() {
        // Set up the frame (main game window)
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);    // Center the window on the screen
        frame.setResizable(true);           // Allow resizing
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit
        frame.setLayout(new BorderLayout()); // Use BorderLayout for the frame

        // Configure the text label
        textLabel = configureJLabel(new JLabel(), Color.darkGray, Color.white, "Tic-Tac-Toe", new Font("Arial", Font.BOLD, 50));
        textPanel = configureJPanel(new JPanel(), Color.darkGray, textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        // Set up the game board panel
        boardPanel = configureJPanel(new JPanel(), Color.darkGray, null);
        boardPanel.setLayout(new GridLayout(3, 3));  // 3x3 grid layout
        frame.add(boardPanel, BorderLayout.CENTER);  

        // Setting up and configuring the reset panel/button
        resetButton = configureJButton(new JButton(), Color.black, Color.white, "Reset Board");
        resetPanel = configureJPanel(new JPanel(), Color.darkGray, resetButton);

        restartButton = configureJButton(new JButton(), Color.black, Color.white, "Restart Game");
        restartPanel = configureJPanel(new JPanel(), Color.darkGray, restartButton);

        exitButton = configureJButton(new JButton(), Color.black, Color.red, "Exit Game");
        exitPanel = configureJPanel(new JPanel(), Color.darkGray, exitButton);

        // Configuring and adding labels for wins and ties
        playerXWinsLabel = configureJLabel(new JLabel(), Color.black, Color.white, "Player X Wins: " + playerXWins, new Font("Arial", Font.BOLD, 18));
        playerXWinsPanel = configureJPanel(new JPanel(), Color.darkGray, playerXWinsLabel);

        playerOWinsLabel = configureJLabel(new JLabel(), Color.black, Color.white, "Player O Wins: " + playerOWins, new Font("Arial", Font.BOLD, 18));
        playerOWinsPanel = configureJPanel(new JPanel(), Color.darkGray, playerOWinsLabel);

        tiesLabel = configureJLabel(new JLabel(), Color.black, Color.white, "Ties: " + numberOfTies, new Font("Arial", Font.BOLD, 18));
        tiesPanel = configureJPanel(new JPanel(), Color.darkGray, tiesLabel);

        // Assemble bottom panel
        bottomPanel = configureJPanel(new JPanel(), Color.darkGray, null);
        bottomPanel.setLayout(new GridLayout(2, 3)); // Organize panels in a grid layout
        bottomPanel.add(playerXWinsPanel);
        bottomPanel.add(playerOWinsPanel);
        bottomPanel.add(tiesPanel);
        bottomPanel.add(resetPanel);
        bottomPanel.add(restartPanel);
        bottomPanel.add(exitPanel);
        frame.add(bottomPanel, BorderLayout.SOUTH);


        // Configuring and adding the bottom panel (use GridBagLayout for flexibility)
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(Color.darkGray);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(tiesPanel, gbc);

        // Player O wins label (aligned to the left)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(playerOWinsPanel, gbc);

        // Player X wins label (centered)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomPanel.add(playerXWinsPanel, gbc);

        // Reset Board Button (centered)
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomPanel.add(resetPanel, gbc);

        // Restart button (aligned to the right)
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(restartPanel, gbc);

        // Exit button (aligned to the far right)
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(exitPanel, gbc);

        // Adding the bottom panel to the frame
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Action listener(button) for the reset btton
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();  // Reset the game board (you need to define this method)
                textLabel.setText("X's Turn");  // Reset the label text
            }
        });

        // Action listner(button) for the restart button
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();  // Reset the game board (you need to define this method)
                textLabel.setText("X's Turn");  // Reset the label text
            }
        });

        // Action listener(button) for the exit button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        // Looping through the Tic-tac-toe grid and adding buttons and listeners to each square
        initializeBoard();
    }

    // Method to check if there is a winner
    public void checkWinner() {

        /*Check horizontal lines for a winner by looping through the board and checking if there is a horizontal line
        of the same characters */ 
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText() == "") continue;
            if (board[r][0].getText() == board[r][1].getText() && board[r][1].getText() == board[r][2].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]);
                }
                gameOver = true;
                if (board[r][0].getText() == playerX) {
                    playerXWins++;
                    playerXWinsLabel.setText("Player X Wins: " + playerXWins); // Update label
                } else {
                    playerOWins++;
                    playerOWinsLabel.setText("Player O Wins: " + playerOWins); // Update label
                }
                return;
            }
        }
    
        // Check vertical lines for a winner (same logic as horizontal)
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText() == "") continue;
            if (board[0][c].getText() == board[1][c].getText() && board[1][c].getText() == board[2][c].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
                gameOver = true;
                if (board[0][c].getText() == playerX) {
                    playerXWins++;
                    playerXWinsLabel.setText("Player X Wins: " + playerXWins);
                } else {
                    playerOWins++;
                    playerOWinsLabel.setText("Player O Wins: " + playerOWins);
                }
                return;
            }
        }
    
        // Check diagonal lines for a winner 
        if (board[0][0].getText() != "" && board[0][0].getText() == board[1][1].getText() && board[1][1].getText() == board[2][2].getText()) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
            gameOver = true;
            if (board[0][0].getText() == playerX) {
                playerXWins++;
                playerXWinsLabel.setText("Player X Wins: " + playerXWins);
            } else {
                playerOWins++;
                playerOWinsLabel.setText("Player O Wins: " + playerOWins);
            }
            return;
        }
    
        if (board[0][2].getText() != "" && board[0][2].getText() == board[1][1].getText() && board[1][1].getText() == board[2][0].getText()) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][2 - i]);
            }
            gameOver = true;
            if (board[0][2].getText() == playerX) {
                playerXWins++;
                playerXWinsLabel.setText("Player X Wins: " + playerXWins);
            } else {
                playerOWins++;
                playerOWinsLabel.setText("Player O Wins: " + playerOWins);
            }
            return;
        }

        if(turns == 9){
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    setTie(board[r][c]);
                }
            }
            gameOver = true;
            textLabel.setText("Tie");
            tiesLabel.setText("Ties: " + ++numberOfTies);
        }
    }

    // Method to highlight the winner's tiles green and display the winner
    public void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }

    // Method to highlight tiles orange in case of a tie
    public void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
    }

    //Method to reset board by reseting some game state variables and clearing the board
    public void resetBoard(){
        gameOver = false;
        currentPlayer = playerX;
        turns = 0;
        textLabel.setText(currentPlayer + "'s turn");
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
            }
        }
    }

    // Method to restart game by resetting all variables and reseting the board
    public void restartGame(){
        playerOWins = 0;
        playerXWins = 0;
        numberOfTies = 0;
        tiesLabel.setText("Ties: " + numberOfTies);
        playerXWinsLabel.setText("Player X Wins: " + playerXWins);
        playerOWinsLabel.setText("Player O Wins: " + playerOWins);
        resetBoard();
    }

    // Helper methods to streamline GUI components declaration and initilization
    private JButton configureJButton(JButton button, Color bgColor, Color fgColor, String text ){
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setText(text);
        button.setFocusable(false);
        return button;
    }

    private JLabel configureJLabel(JLabel label, Color bgColor, Color fgColor, String text, Font font){
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setText(text);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setFocusable(false);
        return label;
    }

    private JPanel configureJPanel(JPanel panel, Color bgColor, Component component){
        panel.setBackground(bgColor);
        if(component != null)
            panel.add(component);
        panel.setFocusable(false);
        return panel;
    }

    private void initializeBoard(){
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = createGameTile(currentPlayer); // Create the tile using the utility method
                boardPanel.add(board[r][c]); // Add the tile to the board
            }
        }
    }
    private JButton createGameTile(String playerSymbol) {
        JButton tile = new JButton();
        tile.setBackground(Color.darkGray);
        tile.setForeground(Color.white);
        tile.setFont(new Font("Arial", Font.BOLD, 120));
        tile.setFocusable(false);
        tile.setText("");  // Empty text initially
        tile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) return;
                JButton clickedTile = (JButton) e.getSource();
                if (clickedTile.getText().isEmpty()) {
                    clickedTile.setText(currentPlayer);
                    turns++;
                    checkWinner();
                    if (!gameOver) {
                        currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                        textLabel.setText(currentPlayer + "'s turn"); // Update the status text
                    }
                }
            }
        });
        return tile;
    }
    
}
