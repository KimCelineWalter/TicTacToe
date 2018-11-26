/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mywsdl.TTTWebService;
import tictactoe.TicTacToeGame.TicTacToePanel;

/**
 *
 * @author Patryk
 */
public class NextMoveThread extends Thread {
//public class ThreadPanel implements Runnable {    

    private TTTWebService proxy;
    private int gameID;
    private int playerNum;
//    private int movesByNow;
    TicTacToePanel panel;
    
    public NextMoveThread() {
        
    }
    
    public NextMoveThread(int gameID, TTTWebService proxy, int playerNum, TicTacToePanel panel) {
       this.proxy = proxy;
       this.gameID = gameID;
       this.playerNum = playerNum;
       this.panel = panel;
       //this.movesByNow = movesByNow;
    }
    
      
    @Override
//    public void run() {
//        int interval = 1000;
//        int oddOrEven = 0;
//        int moves = movesByNow;
//        if(playerNum == 2) {
//            oddOrEven = 1;
//        }
//        while(moves % 2 != oddOrEven) {
//            try {
//                sleep(interval);
//            } catch(Exception e ) {
//            
//            moves = countMoves(proxy.getBoard(gameID));
//            
//            }
//        }
//    }
    
    
    
    public void run() {
        System.out.println(getGameState(gameID));
        System.out.println(gameID);
        while(getGameState(gameID) == 0) {
            if(playerNum == 1) {
                WaitForNextMove(0);
            }
            else if(playerNum == 2) {
                WaitForNextMove(1);
            }
            //panel.fillBoard();
        }

    }
    
    
    private int WaitForNextMove(int i) {
        int moves = countMoves(proxy.getBoard(gameID));
        //System.out.println(moves);
        if(playerNum == 1) {
            System.out.println(moves);
        }
        int interval = 1000;
        panel.fillBoard();
        if(moves % 2 != i) { // player 1 waits for his move
            panel.enableComponents(panel, false);
            while(moves % 2 != i) { 
                try {
                    sleep(interval);
                } catch(Exception e ) {
                }
            moves = countMoves(proxy.getBoard(gameID));
            }
            panel.enableComponents(panel, true);
            panel.fillBoard();
        }
        return moves;
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
    
    public int getGameState(int gameID) {
        String line = proxy.getGameState(gameID);
        int state = -2;
        try { 
            state = Integer.parseInt(line);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, line);
        }
        return state;
    }
}
