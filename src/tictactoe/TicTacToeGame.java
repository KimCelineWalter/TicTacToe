/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeGame extends JFrame{
 private JButton [][] buttons= new JButton[3][3];
 private JLabel label_info =new JLabel("PLAY GAME");
 private TicTacToePanel panel;
 private TTTWebService_Service link;
 private TTTWebService proxy;
 private int userID=0;
 private int gameID=0;
 private String symbol;
 private String player2Symbol;
 private int playerNum = 2;
 
 public TicTacToeGame(int uID, int gID){
     
     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     userID=uID;
     gameID=gID;
     
     setTitle("Tic Tac Toe!");
     setBounds(500,500,400,400);
     setLayout(new BorderLayout());
 
     add(label_info, BorderLayout.NORTH);

     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setVisible(true);
     PlayGame();
     
 
    
 
 }
 
 public void PlayGame(){
 
     String result=proxy.getGameState(gameID);
     System.out.println(result);
     //case 1: client is player 1 and second player joined the game
     panel=new TicTacToePanel();
     add(panel,BorderLayout.CENTER);
     if(result.equals("-1")){
        label_info.setText("Waiting for second Player");
        playerNum = 1;
        symbol = "X";
        player2Symbol = "O";
        NewGameThread newGameThread = new NewGameThread(gameID, proxy);
        newGameThread.start();
        try {
            newGameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Game started--------------------------------------- " + gameID);
    }
    //case 2: client is player 2 and joined the created game from player 1
    else if(result.equals("0")) {  
        playerNum = 2;
        symbol = "O";
        player2Symbol = "X";
    }
    NextMoveThread thread = new NextMoveThread(gameID, proxy, playerNum, panel);
    thread.start();
     // Game starts
 }

    class TicTacToePanel extends JPanel implements ActionListener{
        public TicTacToePanel() {
          setLayout(new GridLayout(3,3));
          //setBounds(300,300,200,200);
          for(int i=0;i<3;i++){
              for(int j=0;j<3;j++) {
               buttons[i][j]=new JButton();
               buttons[i][j].addActionListener(this);
               add(buttons[i][j]);        
              } 
          }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            int x = 0;
            int y = 0;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(e.getSource().equals(buttons[i][j])) {
                        x = i;
                        y = j;
                    }
                }
            }
            
            int moves = getMoves();

            //if(playerNum == 1) { // player1
            waitForNextMove(moves, x, y);
          
            //}
            //else { // player2
                
            //}
        }
        
        public void waitForNextMove(int moves, int x, int y) {
            boolean squareCheckSuccess = false;
            squareCheckSuccess = checkSquare(moves, x, y);
            if(squareCheckSuccess) {
                //enableComponents(this, false);
                //NextMoveThread thread = new NextMoveThread(gameID, proxy, playerNum, moves);
                //thread.start();
                //enableComponents(this, true);
            }
        }
        
        public void enableComponents(Container container, boolean enable) {
            Component[] components = container.getComponents();
            for (Component component : components) {
                component.setEnabled(enable);
                if (component instanceof Container) {
                    enableComponents((Container)component, enable);
                }
        }
    }
        
        public boolean checkSquare(int moves, int x, int y) {
            int squareChecked = 1;
            int successInt = 0;
            boolean success = false;
            try {
                squareChecked = Integer.parseInt(proxy.checkSquare(x, y, gameID));               
                if(squareChecked == 0) {
                    try {
                        successInt = Integer.parseInt(proxy.takeSquare(x, y, gameID, userID));   
                        if(successInt != 1) {
                            JOptionPane.showMessageDialog(null, "Error!");
                        } 
                        else {
                            buttons[x][y].setText(symbol);
                            success = true;
                        }
                    } catch(Exception ex) {
                        JOptionPane.showMessageDialog(null, proxy.takeSquare(x, y, gameID, userID));
                    }
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, proxy.checkSquare(x, y, gameID));
            }
            return success;
        }
        
        private int countMoves(String line) {
            int moves = 0;
            if(line.equals("ERROR-DB")) {
                JOptionPane.showMessageDialog(null, line);
            }
            else if(!line.equals("ERROR-NOMOVES")){
                String [] array = line.split("\n");
                moves = array.length;
            }
            return moves;
        }
        
        private int getMoves() {
            String line = proxy.getBoard(gameID);
            int noOfMoves = countMoves(line);
            return noOfMoves;
        }
        
        public void fillBoard() {
            String line = proxy.getBoard(gameID);
            int noOfMoves = countMoves(line);
            if(noOfMoves > 0) {
                String [] moves = line.split("\n");
                String [] axis;
                for(int i = 0; i < noOfMoves; i++) {
                    axis = moves[i].split(",");
                    int x = Integer.parseInt(axis[1]);
                    int y = Integer.parseInt(axis[2]);
                    if(axis[0].equals(userID)) {
                        buttons[x][y].setText(symbol);
                    }
                    else {
                        buttons[x][y].setText(player2Symbol);
                    }
                }
            }
        }
    }
    
    
}
