import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Main class for the Tic-Tac-Toe game
public class TicTacToe {

    // Dimensions of the game board
    int boardWidth = 600;
    int boardHeight = 650; // Allocating extra 50 pixels for the text panel at the top

    // GUI components
    JFrame frame = new JFrame("Tic-Tac-Toe"); // Main game window
    JLabel textLabel = new JLabel();         // Label to display the game status or winner
    JPanel textPanel = new JPanel();         // Panel to hold the text label
    JPanel boardPanel = new JPanel();        // Panel to hold the game board (grid of buttons)
    JPanel bottomPanel = new JPanel();
    JLabel exitLabel = new JLabel();            // Panel to hold number of times playerO has won
    JButton resetButton = new JButton();
    JPanel resetPanel = new JPanel();
    JPanel playerXWinsPanel = new JPanel();
    JLabel playerXWinsLabel = new JLabel();
    JPanel playerOWinsPanel = new JPanel();
    JLabel playerOWinsLabel = new JLabel(); //label

    // 3x3 grid of buttons for the game board
    JButton[][] board = new JButton[3][3];

    // Players and their symbols
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;          // The current player (starts with "X")

    // Game state variables
    boolean gameOver = false;                // Flag to indicate if the game is over
    int turns = 0;                           // Counter to track the number of turns taken

    int playerXWins = 0;        //tracker for the number of times playerX has won
    int playerOWins = 0;        //tracker for the number of times playerO has won

    // Constructor to set up the game
    TicTacToe() {
        // Set up the frame (main game window)
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);    // Center the window on the screen
        frame.setResizable(true);           // Allow resizing
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit
        frame.setLayout(new BorderLayout()); // Use BorderLayout for the frame

        // Configure the text label
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER); // Center-align text
        textLabel.setText("Tic-Tac-Toe");               // Initial text
        textLabel.setOpaque(true);                     // Make the background color visible

        // Add the text label to the text panel
        // Text label (Game status) panel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        frame.add(textPanel, BorderLayout.NORTH);  // This should already be working, but double check


        // Set up the game board panel
        boardPanel.setLayout(new GridLayout(3, 3));  // 3x3 grid layout
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel, BorderLayout.CENTER);  // Use CENTER so it takes available space

        // Setting up and configuring the reset panel/button
        resetButton.setFont(new Font("Arial", Font.BOLD, 50));
        resetButton.setText("Reset Board");
        resetButton.setBackground(Color.black);
        resetButton.setForeground(Color.white);
        resetPanel.setBackground(Color.darkGray);
        resetPanel.add(resetButton); 
        bottomPanel.add(resetPanel, BorderLayout.EAST);

        // Setting up and configuring the player wins counter labels
        playerOWinsLabel.setFont(new Font("Arial", Font.BOLD, 50));
        playerXWinsLabel.setFont(new Font("Arial", Font.BOLD, 50));

        // Include initial win counts in the label text
        playerOWinsLabel.setText("Player O Wins: " + playerOWins);
        playerXWinsLabel.setText("Player X Wins: " + playerXWins);

        // Set the background and foreground colors
        playerOWinsLabel.setBackground(Color.black);
        playerXWinsLabel.setBackground(Color.black);
        playerOWinsLabel.setForeground(Color.white);
        playerXWinsLabel.setForeground(Color.white);

        // Make the background color visible
        playerOWinsLabel.setOpaque(true);
        playerXWinsLabel.setOpaque(true);

        // Configure and add the labels to their respective panels
        playerOWinsPanel.setBackground(Color.darkGray); // Set panel background
        playerOWinsPanel.add(playerOWinsLabel);         // Add label to panel

        playerXWinsPanel.setBackground(Color.darkGray); // Set panel background
        playerXWinsPanel.add(playerXWinsLabel);         // Add label to panel

        // Configuring and adding the bototm panel
        // Configuring and adding the bottom panel (use GridBagLayout for flexibility)
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Player O wins label (aligned to the left)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(playerOWinsPanel, gbc);

        // Player X wins label (centered)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomPanel.add(playerXWinsPanel, gbc);

        // Reset button (aligned to the right)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(resetPanel, gbc);

        // Adding the bottom panel to the frame
        frame.add(bottomPanel, BorderLayout.SOUTH);


        // Adding the bottom panel to the frame
        frame.add(bottomPanel, BorderLayout.SOUTH);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();  // Reset the game board (you need to define this method)
                textLabel.setText("X's Turn");  // Reset the label text
            }
        });
        

        // Initialize the game board with buttons
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();      // Create a new button
                board[r][c] = tile;               // Add it to the board array
                boardPanel.add(tile);             // Add it to the board panel

                // Configure the button appearance
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120)); // Large font for "X" or "O"
                tile.setFocusable(false);                       // Disable focus highlight

                // Add a click listener for the button
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;                   // Ignore clicks if the game is over
                        JButton tile = (JButton) e.getSource(); // Get the clicked button
                        if (tile.getText() == "") {             // Check if the button is empty
                            tile.setText(currentPlayer);        // Set the text to the current player's symbol
                            turns++;                            // Increment the turn counter
                            checkWinner();                      // Check if there is a winner
                            if (!gameOver) {
                                // Switch to the next player
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn"); // Update the status text
                            }
                        }
                    }
                });
            }
        }
    }

    // Method to check if there is a winner
    public void checkWinner() {
        // Check horizontal lines for a winner
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText() == "") continue; // Skip empty rows
            if (board[r][0].getText() == board[r][1].getText() && board[r][1].getText() == board[r][2].getText()) {
                // Mark the winning row
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]); // Highlight the winning row
                }
                gameOver = true; // Set game over
                if (board[r][0].getText() == playerX) {
                    playerXWins++; // Increment Player X wins
                    playerXWinsLabel.setText("Player X Wins: " + playerXWins); // Update label
                } else {
                    playerOWins++; // Increment Player O wins
                    playerOWinsLabel.setText("Player O Wins: " + playerOWins); // Update label
                }
                return; // Exit the method after a winner is found
            }
        }
    
        // Check vertical lines for a winner (same logic as horizontal)
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText() == "") continue; // Skip empty columns
            if (board[0][c].getText() == board[1][c].getText() && board[1][c].getText() == board[2][c].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]); // Highlight the winning column
                }
                gameOver = true;
                if (board[0][c].getText() == playerX) {
                    playerXWins++;
                    playerXWinsLabel.setText("Player X Wins: " + playerXWins); // Update label
                } else {
                    playerOWins++;
                    playerOWinsLabel.setText("Player O Wins: " + playerOWins); // Update label
                }
                return;
            }
        }
    
        // Check diagonal lines for a winner
        if (board[0][0].getText() != "" && board[0][0].getText() == board[1][1].getText() && board[1][1].getText() == board[2][2].getText()) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]); // Highlight the winning diagonal
            }
            gameOver = true;
            if (board[0][0].getText() == playerX) {
                playerXWins++;
                playerXWinsLabel.setText("Player X Wins: " + playerXWins); // Update label
            } else {
                playerOWins++;
                playerOWinsLabel.setText("Player O Wins: " + playerOWins); // Update label
            }
            return;
        }
    
        if (board[0][2].getText() != "" && board[0][2].getText() == board[1][1].getText() && board[1][1].getText() == board[2][0].getText()) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][2 - i]); // Highlight the winning diagonal
            }
            gameOver = true;
            if (board[0][2].getText() == playerX) {
                playerXWins++;
                playerXWinsLabel.setText("Player X Wins: " + playerXWins); // Update label
            } else {
                playerOWins++;
                playerOWinsLabel.setText("Player O Wins: " + playerOWins); // Update label
            }
            return;
        }
    }

    // Method to highlight the winner's tiles
    public void setWinner(JButton tile) {
        tile.setForeground(Color.green);  // Change text color to green
        tile.setBackground(Color.gray);   // Change background color
        textLabel.setText(currentPlayer + " is the winner!"); // Display the winner
    }

    // Method to highlight tiles in case of a tie
    public void setTie(JButton tile) {
        tile.setForeground(Color.orange); // Change text color to orange
        tile.setBackground(Color.gray);   // Change background color
        textLabel.setText("Tie");         // Display tie message
    }

    //Method to reset board
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
}
